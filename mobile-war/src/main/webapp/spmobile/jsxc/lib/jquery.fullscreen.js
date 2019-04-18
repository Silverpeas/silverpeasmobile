/*
 * Copyright (C) 2000 - 2019 Silverpeas
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

/**
 * @name        jQuery Fullscreen Plugin
 * @author      Klaus Herberth, Martin Angelov, Morten Sjøgren
 * @url         http://tutorialzine.com/2012/02/enhance-your-website-fullscreen-api/
 * @license     MIT License
 */

/*jshint browser: true, jquery: true */
(function($) {
    "use strict";

    // These helper functions available only to our plugin scope.
    function supportFullscreen() {
        var doc = document.documentElement;

        return ('requestFullscreen' in doc) ||
                ('mozRequestFullScreen' in doc && document.mozFullScreenEnabled) ||
                ('webkitRequestFullscreen' in doc);
    }

    function requestFullscreen(elem) {
        if (elem.requestFullscreen) {
            elem.requestFullscreen();
        } else if (elem.mozRequestFullScreen) {
            elem.mozRequestFullScreen();
        } else if (elem.webkitRequestFullscreen) {
            elem.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
        }
    }

    function fullscreenStatus() {
        return document.fullscreen ||
                document.mozFullScreen ||
                document.webkitIsFullScreen ||
                false;
    }

    function cancelFullscreen() {
        if (document.exitFullscreen) {
            document.exitFullscreen();
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        } else if (document.webkitCancelFullScreen) {
            document.webkitCancelFullScreen();
        }
    }

    // Adding a new test to the jQuery support object
    $.support.fullscreen = supportFullscreen();

    // Creating the plugin
    $.fn.fullscreen = function() {
        if (!$.support.fullscreen || this.length !== 1)
            return this;

        if (fullscreenStatus()) {
            // if we are already in fullscreen, exit
            cancelFullscreen();
            return this;
        }
        
        var self = this;

        // Chrome trigger event on self, Firefox on document
        $(self).add(document).on('fullscreenerror mozfullscreenerror webkitfullscreenerror msfullscreenerror', function() {
            $(document).trigger('error.fullscreen');
        });

        $(self).add(document).on('fullscreenchange mozfullscreenchange webkitfullscreenchange msfullscreenchange', function() {
            if (fullscreenStatus()){ 
                $(document).trigger('enabled.fullscreen');
            }else{
                $(document).trigger('disabled.fullscreen');
                $(self).add(document).off('fullscreenchange mozfullscreenchange webkitfullscreenchange msfullscreenchange');
            }
        });

        requestFullscreen($(self).get(0));

        return $(self);
    };

    $.fn.cancelFullscreen = function( ) {
        cancelFullscreen();

        return this;
    };
}(jQuery));