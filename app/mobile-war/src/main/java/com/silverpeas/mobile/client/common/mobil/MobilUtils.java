package com.silverpeas.mobile.client.common.mobil;

import com.google.gwt.dom.client.Document;
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
      return getDevicePixelRatio();
    }
    return false;
  }

  public static boolean isWVGA() {
    return Document.get().getDocumentElement().getClassName().contains("WVGA");
  }

  public static final native boolean getDevicePixelRatio() /*-{
    return ($wnd.devicePixelRatio >= 2);
  }-*/;

  public static final native boolean isStandalone() /*-{
    return $wnd.navigator.standalone;
  }-*/;

  public static boolean isMobil() {
    return isAndroid() || isIOS();
  }
}
