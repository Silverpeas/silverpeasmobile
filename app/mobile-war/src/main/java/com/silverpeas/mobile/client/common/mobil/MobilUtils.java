package com.silverpeas.mobile.client.common.mobil;

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
