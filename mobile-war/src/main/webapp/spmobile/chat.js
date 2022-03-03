/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

function avatars() {

  $(".jsxc_avatar").each(function() {

    var bid = $(this).parent().attr("data-bid");
    if (bid == "") {

    }

    if (!bid.includes("conference")) {
      $(this).text("");
      var login = bid.substr(0, bid.indexOf("@"));
      $(this).css("background-image", "url('/silverpeas/display/avatar/60x//" + login + ".jpg')");

    }
  });

}

function avatar() {

  $(".jsxc_windowItem").each(function() {

    var bid = $(this).attr("data-bid");

    var login = bid.substr(0, bid.indexOf("@"));

    $(".jsxc_avatar").each(function() {
      $(this).text("");
      $(this).css("background-image", "url('/silverpeas/display/avatar/60x//" + login + ".jpg')");
    });

  });

}

jsxc.gui.window.close = function(bid) {

  if (jsxc.gui.window.get(bid).length === 0) {
    jsxc.warn('Want to close a window, that is not open.');
    return;
  }

  jsxc.storage.removeUserElement('windowlist', bid);
  jsxc.storage.removeUserItem('window', bid);

  if (jsxc.storage.getUserItem('buddylist').indexOf(bid) < 0) {
    // delete data from unknown sender

    jsxc.storage.removeUserItem('buddy', bid);
    jsxc.storage.removeUserItem('chat', bid);
  }

  jsxc.gui.window._close(bid);

  /* Patch */
  $('#jsxc_roster').width('100%');
  jsxc.gui.roster.toggle();
  avatars();
}

jsxc.gui.window.hide = function(bid) {

}

jsxc.gui.window.fullsize = function(bid) {
  var win = jsxc.gui.window.get(bid);
  var size = jsxc.options.viewport.getSize();

  //size.width -= 10;
  size.height -= win.find('.jsxc_bar').outerHeight() + win.find('.jsxc_textinput').outerHeight();
  jsxc.gui.window.resize(win, {
    size : size
  });
  avatar();
}

jsxc.gui.scrollWindowListBy = function(offset) {
  var el = $('#jsxc_windowList>ul');
  el.css('right', '0px');
  el.css('left', '0px');
}

$(document).ready(function() {
  var settings = {
    xmpp : {
      url : '/http-bind/', domain : 'localhost', resource : 'spmobile', overwrite : true
    }
  };

  // Initialize core functions, intercept login form
  // and attach connection if possible.
  jsxc.init({
    logoutElement : $('#logout'),
    rosterAppend : 'body',
    root : 'jsxc',
    displayRosterMinimized : function() {
      return false;
    },
    loadSettings : function(username, password, cb) {
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

  $(document).on('ready-roster-jsxc', function() {
    $('#jsxc_buddylist').width('100%');
    $('#jsxc_roster').width('100%');
    $('.slimScrollDiv').width('100%');
    $("#jsxc_windowListSB").width('auto');

    $("#jsxc_windowListSB>ul").css('left', '0px');
    $("#jsxc_windowListSB>ul").css('right', '0px');

  });

  $(document).on('cloaded.roster.jsxc', function() {
    avatars();
  });

  $(document).on('show.window.jsxc', function() {
    $('#jsxc_roster').width('0px');
    $('.jsxc_windowItem jsxc_normal').width('100%');
    avatars();
  });

  $(document).on('hidden.window.jsxc', function() {

  });

  var maindomain = '@' + settings.xmpp.domain.split('.').reverse().splice(0,2).reverse().join('.');
  if (username.includes(maindomain)) {
    jsxc.start(username, password);
  } else {
    jsxc.start(username + maindomain, password);
  }
  $('#password').val('');
});


