/* Run via a local HTTP server at /tests/pwa-install.html. No test dependencies required. */
(async function () {
  const source = await fetch('../mobile-war/src/main/webapp/spmobile/pwa-install.js').then(r => r.text());
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
    await test('iPhone, iPadOS, Android and desktop instruction routing predicates', async () => {
      const java = await fetch('../mobile-war/src/main/java/org/silverpeas/mobile/client/common/PWAHelper.java').then(r => r.text());
      function native(name) {
        const pattern = new RegExp('private static native boolean ' + name + '\\(\\) /\\*-\\{([\\s\\S]*?)\\}-\\*/;');
        const match = java.match(pattern); assert(match, 'Native method missing: ' + name);
        return new Function('$wnd', match[1]);
      }
      const ios = native('isIos'), safari = native('isSafari'), secure = native('isSecureContext');
      const iphone = { navigator: { userAgent: 'iPhone Version/18.0 Mobile Safari/605.1', platform: 'iPhone' } };
      assert(ios(iphone) && safari(iphone), 'iPhone Safari route');
      const chromeIos = { navigator: { userAgent: 'iPhone CriOS/130.0 Mobile Safari/605.1', platform: 'iPhone' } };
      assert(ios(chromeIos) && !safari(chromeIos), 'iPhone Chrome route');
      assert(ios({navigator:{userAgent:'Macintosh Safari',platform:'MacIntel',maxTouchPoints:5}}), 'iPadOS route');
      assert(!ios({navigator:{userAgent:'Android Chrome',platform:'Linux armv8l'}}), 'Android route');
      assert(!ios({navigator:{userAgent:'Macintosh Safari',platform:'MacIntel',maxTouchPoints:0}}), 'Desktop route');
      assert(!secure({isSecureContext:false}) && secure({isSecureContext:true}), 'HTTPS requirement');
    });
    await test('Manifest icons include usable 192 and 512 pixel assets', async () => {
      const manifest = await fetch('../mobile-war/src/main/webapp/WEB-INF/manifest.json.template').then(r => r.json());
      for (const size of [192, 512]) {
        const icon = manifest.icons.find(icon => icon.sizes === size + 'x' + size);
        assert(icon, 'Missing ' + size + ' icon');
        const image = new Image();
        await new Promise((resolve, reject) => {
          image.onload = resolve; image.onerror = () => reject(new Error('Icon did not load'));
          image.src = '../mobile-war/src/main/webapp/spmobile/' + icon.src;
        });
        assert(image.naturalWidth === size && image.naturalHeight === size, 'Wrong icon dimensions');
      }
    });
    document.getElementById('results').textContent = results.join('\n') + '\nAll 13 tests passed';
  } catch (error) {
    document.getElementById('results').textContent = results.join('\n') + '\nFAIL: ' + error.stack;
  }
})();
