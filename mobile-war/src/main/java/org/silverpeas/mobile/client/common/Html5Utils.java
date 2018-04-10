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

package org.silverpeas.mobile.client.common;

import com.google.gwt.dom.client.Element;

/**
 * @author: svu
 */
public class Html5Utils {

  public static native void setVideoFullScreen(Element element) /*-{
      if (element.requestFullscreen) {
          element.requestFullscreen();
      } else if (element.msRequestFullscreen) {
          element.msRequestFullscreen();
      } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen();
      } else if (element.webkitRequestFullscreen) {
          element.webkitRequestFullscreen();
      }
  }-*/;

  public static native void setVideoNotFullScreen(Element element) /*-{
      if (element.exitFullscreen) {
          element.exitFullscreen();
      } else if (element.mozCancelFullScreen) {
          element.mozCancelFullScreen();
      } else if (element.webkitExitFullscreen) {
          element.webkitExitFullscreen();
      }
  }-*/;

  public static native boolean isVideoFullScreen() /*-{
      var fullscreenEnabled = document.fullscreenEnabled || document.mozFullScreenEnabled || document.webkitFullscreenEnabled;
      return fullscreenEnabled;
  }-*/;

  public static void vibrate() {
    vibrate(300);
  }

  public static native void vibrate(int duration) /*-{
    $wnd.navigator.vibrate(duration);
  }-*/;
}
