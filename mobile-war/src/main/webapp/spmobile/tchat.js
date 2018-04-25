/*
 * Copyright (C) 2000 - 2018 Silverpeas
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

$( document ).ready(function() {
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

  // authentication variable
  var url = $('#bosh-url').val();
  var domain = $('#xmpp-domain').val();
  var username = $('#username').val();
  var password = $('#password').val();

  if (!url || !domain) {
    jsxc.log = jsxc.log + "we need url and domain to test BOSH server\n";
    return;
  }
  jsxc.options.xmpp.url = url;
  settings.xmpp.url = url;
  settings.xmpp.domain = domain;

  $(document).on('connecting.jsxc', function() {

  });

  $(document).on('authfail.jsxc', function() {
    alert('authentication fail !');
  });

  $(document).on('attached.jsxc', function() {
    jsxc.gui.roster.toggle('shown');
  });

  $(document).on('disconnected.jsxc', function() {

  });

  jsxc.start(username + '@' + settings.xmpp.domain, password);
  $('#password').val('');
});


