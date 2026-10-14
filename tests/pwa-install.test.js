/* Run via a local HTTP server at /tests/pwa-install.html. No test dependencies required. */
(async function () {
  let source, assetBase;
  async function loadSource() {
    const errors = [];
    // Support both /silverpeas/tests/ in a deployment and /tests/ at the repository root.
    for (const base of ['../spmobile/', '../mobile-war/src/main/webapp/spmobile/']) {
      const url = new URL(base + 'pwa-install.js', location.href);
      try {
        const response = await fetch(url);
        const text = await response.text();
        if (!response.ok || /^\s*</.test(text) || !text.includes('root.spMobilePwa')) {
          throw new Error('HTTP ' + response.status + ': expected PWA JavaScript, received '
            + (response.headers.get('content-type') || 'an unexpected document'));
        }
        source = text; assetBase = new URL(base, location.href); return;
      } catch (error) { errors.push(url.href + ': ' + error.message); }
    }
    throw new Error('Cannot load pwa-install.js. Check the deployment path and login session.\n' + errors.join('\n'));
  }
  async function loadManifest() {
    const candidates = [new URL('manifest.json', assetBase),
      new URL('../mobile-war/src/main/webapp/WEB-INF/manifest.json.template', location.href)];
    const errors = [];
    for (const url of candidates) {
      try {
        const response = await fetch(url); const text = await response.text();
        if (!response.ok || /^\s*</.test(text)) throw new Error('HTTP ' + response.status + ': expected a JSON manifest');
        return { manifest: JSON.parse(text), url: url };
      } catch (error) { errors.push(url.href + ': ' + error.message); }
    }
    throw new Error('Cannot load the manifest. Check the login session and manifest endpoint.\n' + errors.join('\n'));
  }
  const results = [];
  function assert(value, message) { if (!value) throw new Error(message); }
  function setup(options = {}) {
    const handlers = {};
    const queries = {};
    const root = {
      navigator: { standalone: !!options.iosStandalone },
      console: { warn() {} },
      addEventListener(name, handler) { (handlers[name] ||= []).push(handler); },
      matchMedia(query) {
        if (!queries[query]) queries[query] = {
          matches: query === '(display-mode: ' + options.mode + ')',
          addEventListener(name, handler) { this.changed = handler; }
        };
        return queries[query];
      }
    };
    function fire(name, event = {}) { (handlers[name] || []).forEach(handler => handler(event)); }
    new Function('window', source)(root);
    return { root, api: root.spMobilePwa, fire, queries };
  }
  function event(prompt = () => Promise.resolve(), outcome = 'dismissed') {
    return { preventDefault() { this.prevented = true; }, prompt,
      userChoice: Promise.resolve({ outcome }) };
  }
  async function settle() { for (let i = 0; i < 5; ++i) await Promise.resolve(); }
  async function test(name, run) { await run(); results.push('PASS: ' + name); }
  try {
    await loadSource();
    await test('No event does not mean already installed', () => {
      const { api } = setup(); assert(!api.isInstalled(), 'Wrong installed status');
      assert(!api.canInstall(), 'No prompt should be available');
    });
    await test('Late event updates the UI subscriber', () => {
      const { api, fire } = setup(); let called = 0;
      api.subscribe(() => ++called); const e = event(); fire('beforeinstallprompt', e);
      assert(called === 2 && api.canInstall() && e.prevented, 'Late event lost');
    });
    await test('Early event is preserved until GWT subscribes', () => {
      const { api, fire } = setup(); fire('beforeinstallprompt', event());
      let ready = false; api.subscribe(() => ready = api.canInstall());
      assert(ready, 'Early event lost');
    });
    await test('Standalone, minimal UI, fullscreen and iOS standalone detected', () => {
      for (const mode of ['standalone', 'minimal-ui', 'fullscreen']) {
        assert(setup({ mode }).api.isInstalled(), mode + ' not detected');
      }
      assert(setup({ iosStandalone: true }).api.isInstalled(), 'iOS not detected');
    });
    await test('Display mode changes notify the menu', () => {
      const { api, queries } = setup(); let installed = false;
      api.subscribe(() => installed = api.isInstalled());
      const query = queries['(display-mode: standalone)']; query.matches = true; query.changed();
      assert(installed, 'Menu did not update');
    });
    await test('Native prompt is called synchronously and only once', async () => {
      const { api, fire } = setup(); let calls = 0, fallback = 0;
      fire('beforeinstallprompt', event(() => { ++calls; return Promise.resolve(); }));
      api.prompt(() => ++fallback); assert(calls === 1, 'User gesture was lost');
      api.prompt(() => ++fallback); assert(calls === 1 && fallback === 0, 'Duplicate prompt');
      await settle(); assert(!api.canInstall() && fallback === 0, 'Dismissal handled incorrectly');
    });
    await test('Unavailable native prompt uses manual instructions', () => {
      const { api } = setup(); let fallback = 0; api.prompt(() => ++fallback);
      assert(fallback === 1, 'Manual instructions not requested');
    });
    await test('Synchronous prompt error uses manual instructions', () => {
      const { api, fire } = setup(); let fallback = 0;
      fire('beforeinstallprompt', event(() => { throw Error('Unavailable'); }));
      api.prompt(() => ++fallback); assert(fallback === 1 && !api.canInstall(), 'Error not handled');
    });
    await test('Rejected native prompt uses manual instructions', async () => {
      const { api, fire } = setup(); let fallback = 0;
      fire('beforeinstallprompt', event(() => Promise.reject(Error('Unavailable'))));
      api.prompt(() => ++fallback); await settle(); assert(fallback === 1, 'Rejection not handled');
    });
    await test('Successful installation hides the install action', () => {
      const { api, fire } = setup(); let installed = false;
      api.subscribe(() => installed = api.isInstalled());
      fire('beforeinstallprompt', event()); fire('appinstalled');
      assert(installed && !api.canInstall(), 'Installed state not updated');
    });
    await test('Menu unsubscribe and repeated initialization do not leak listeners', () => {
      const { api, root, fire } = setup(); let called = 0;
      const remove = api.subscribe(() => ++called); remove();
      new Function('window', source)(root); fire('beforeinstallprompt', event());
      assert(called === 1 && root.spMobilePwa === api, 'Listener leak');
    });
    await test('iPhone, iPadOS, Android and desktop instruction routing predicates', () => {
      function platform(userAgent, platform, maxTouchPoints = 0) {
        const { api, root } = setup();
        root.navigator.userAgent = userAgent; root.navigator.platform = platform;
        root.navigator.maxTouchPoints = maxTouchPoints; return { api, root };
      }
      const iphone = platform('iPhone Version/18.0 Mobile Safari/605.1', 'iPhone');
      assert(iphone.api.isIos() && iphone.api.isSafari(), 'iPhone Safari route');
      const chromeIos = platform('iPhone CriOS/130.0 Mobile Safari/605.1', 'iPhone');
      assert(chromeIos.api.isIos() && !chromeIos.api.isSafari(), 'iPhone Chrome route');
      assert(platform('Macintosh Safari', 'MacIntel', 5).api.isIos(), 'iPadOS route');
      assert(!platform('Android Chrome', 'Linux armv8l').api.isIos(), 'Android route');
      assert(!platform('Macintosh Safari', 'MacIntel').api.isIos(), 'Desktop route');
      iphone.root.isSecureContext = false; assert(!iphone.api.isSecureContext(), 'HTTP requirement');
      iphone.root.isSecureContext = true; assert(iphone.api.isSecureContext(), 'HTTPS requirement');
    });
    await test('Manifest icons include usable 192 and 512 pixel assets', async () => {
      const { manifest } = await loadManifest();
      for (const size of [192, 512]) {
        const icon = manifest.icons.find(icon => icon.sizes === size + 'x' + size);
        assert(icon, 'Missing ' + size + ' icon');
        const image = new Image();
        await new Promise((resolve, reject) => {
          image.onload = resolve; image.onerror = () => reject(new Error('Icon did not load'));
          image.src = new URL(icon.src, new URL("manifest.json", assetBase)).href;
        });
        assert(image.naturalWidth === size && image.naturalHeight === size, 'Wrong icon dimensions');
      }
    });
    document.getElementById('results').textContent = results.join('\n') + '\nAll 13 tests passed';
  } catch (error) {
    document.getElementById('results').textContent = results.join('\n') + '\nFAIL: ' + error.stack;
  }
})();
