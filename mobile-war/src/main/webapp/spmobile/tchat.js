$( document ).ready(function() {
  //$('#submit').click();
  var settings = {
    xmpp: {
      url: '/http-bind/',
      domain: 'localhost',
      resource: 'spmobile',
      overwrite: true
    }
  };

  // Initialize core functions, intercept login form
  // and attach connection if possible.
  jsxc.init({
    logoutElement: $('#logout'),
    rosterAppend: 'body',
    root: 'jsxc',
    displayRosterMinimized: function() {
      return false;
    },
    loadSettings: function(username, password, cb) {
      cb(settings);
    }
  });

  // helper variable
  var source = '#form';

  var url = $('#bosh-url').val();
  var domain = $('#xmpp-domain').val();
  var username = $('#username').val();
  var password = $('#password').val();
  $('#password').val('');

  if (!url || !domain) {
    jsxc.log = jsxc.log + "we need url and domain to test BOSH server\n";
    return;
  }
  jsxc.options.xmpp.url = url;
  settings.xmpp.url = url;
  settings.xmpp.domain = domain;


  source = $(this);
  $('#submit').button('loading');
  jsxc.start(username + '@' + settings.xmpp.domain, password);

});


