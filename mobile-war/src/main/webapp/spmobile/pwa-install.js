/* Capture installation events before GWT loads. No device-specific installation assumptions. */
(function (root) {
  "use strict";
  if (root.spMobilePwa) return;
  var pendingPrompt = null;
  var installed = false;
  var prompting = false;
  var listeners = [];
  function isInstalled() {
    return installed || root.navigator.standalone === true
      || (root.matchMedia && (root.matchMedia("(display-mode: standalone)").matches
        || root.matchMedia("(display-mode: minimal-ui)").matches
        || root.matchMedia("(display-mode: fullscreen)").matches));
  }
  function notify() {
    listeners.slice().forEach(function (listener) { listener(); });
  }
  root.addEventListener("beforeinstallprompt", function (event) {
    event.preventDefault();
    if (isInstalled()) return;
    pendingPrompt = event;
    notify();
  });
  root.addEventListener("appinstalled", function () {
    installed = true;
    pendingPrompt = null;
    notify();
  });
  if (root.matchMedia) {
    ["standalone", "minimal-ui", "fullscreen"].forEach(function (mode) {
      var query = root.matchMedia("(display-mode: " + mode + ")");
      if (query.addEventListener) query.addEventListener("change", notify);
      else if (query.addListener) query.addListener(notify);
    });
  }
  root.spMobilePwa = {
    isInstalled: isInstalled,
    isSecureContext: function () { return root.isSecureContext === true; },
    isIos: function () {
      var navigator = root.navigator;
      return /iPad|iPhone|iPod/.test(navigator.userAgent)
        || (navigator.platform === "MacIntel" && navigator.maxTouchPoints > 1);
    },
    isSafari: function () {
      var ua = root.navigator.userAgent;
      return /Safari/.test(ua) && !/CriOS|FxiOS|EdgiOS|OPiOS/.test(ua);
    },
    canInstall: function () { return !!pendingPrompt && !prompting && !isInstalled(); },
    subscribe: function (listener) {
      listeners.push(listener);
      listener();
      return function () {
        var index = listeners.indexOf(listener);
        if (index !== -1) listeners.splice(index, 1);
      };
    },
    prompt: function (onUnavailable) {
      if (!pendingPrompt || prompting || isInstalled()) {
        if (!prompting && !isInstalled()) onUnavailable();
        return;
      }
      // Each browser event may be prompted only once. Call synchronously from the user click.
      var event = pendingPrompt;
      pendingPrompt = null;
      prompting = true;
      notify();
      function done() { prompting = false; notify(); }
      function failed(error) {
        done();
        if (root.console) root.console.warn("PWA installation could not be prompted", error);
        onUnavailable();
      }
      try {
        Promise.resolve(event.prompt()).then(function () {
          return event.userChoice;
        }).then(done, failed);
      } catch (error) { failed(error); }
    }
  };
})(window);
