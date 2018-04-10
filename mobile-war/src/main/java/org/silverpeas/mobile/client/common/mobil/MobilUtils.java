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

package org.silverpeas.mobile.client.common.mobil;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;

public class MobilUtils {
  public static Orientation getOrientation() {
    if (Window.getClientHeight() > Window.getClientWidth()) {
      return Orientation.Portrait;
    } else {
      return Orientation.Landscape;
    }
  }

  public static boolean isIOS() {
    return Window.Navigator.getUserAgent().contains("iPhone") ||
        Window.Navigator.getUserAgent().contains("iPod") ||
        Window.Navigator.getUserAgent().contains("iPad");
  }

  public static boolean isAndroid() {
    return Window.Navigator.getUserAgent().contains("Android");
  }

  public static boolean isRetina() {
    if (isIOS()) {
      return isDevicePixelRatioGreaterTwo();
    }
    return false;
  }

  public static boolean isWVGA() {
    return Document.get().getDocumentElement().getClassName().contains("WVGA");
  }

  public static final native boolean isDevicePixelRatioGreaterTwo() /*-{
    return ($wnd.devicePixelRatio >= 2);
  }-*/;

  public static final native boolean isStandalone() /*-{
    return $wnd.navigator.standalone;
  }-*/;

  public static boolean isMobil() {
    return isAndroid() || isIOS();
  }

  public static boolean isTablet() {
    Element e = Document.get().getElementById("oneinch");

    int dpi_x = e.getOffsetWidth();
    int dpi_y = e.getOffsetHeight();
    double w = getScreenWidth() / dpi_x;
    double h = getScreenHeight() / dpi_y ;
    double diag = Math.sqrt(h * h + w * w);

    // diag value is wrong
    // ex : 7" => 11
    //      8" => 12
    //     10" => 15

    return (diag > 10);
  }

  public static final native int getDdvicePixelRatio() /*-{
    var devicePixelRatio = $wnd.devicePixelRatio;

    return devicePixelRatio;
  }-*/;

  public static final native int getScreenWidth() /*-{
    var w = $wnd.screen.width;
    return w;
  }-*/;

  public static final native int getScreenHeight() /*-{
    var h = $wnd.screen.height;
    return h;
  }-*/;

}
