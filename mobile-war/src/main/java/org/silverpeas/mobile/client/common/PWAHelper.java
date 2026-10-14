package org.silverpeas.mobile.client.common;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import org.silverpeas.mobile.client.resources.ApplicationMessages;

public class PWAHelper {
  public static native JavaScriptObject addStateListener(Command listener) /*-{
    if (!$wnd.spMobilePwa) return null;
    return $wnd.spMobilePwa.subscribe($entry(function () {
      listener.@com.google.gwt.user.client.Command::execute()();
    }));
  }-*/;

  public static native void removeStateListener(JavaScriptObject unsubscribe) /*-{
    if (unsubscribe) unsubscribe();
  }-*/;

  public static native boolean isInstalledApp() /*-{
    return !!($wnd.spMobilePwa && $wnd.spMobilePwa.isInstalled());
  }-*/;

  public static native boolean canInstall() /*-{
    return !!($wnd.spMobilePwa && $wnd.spMobilePwa.canInstall());
  }-*/;

  public static void installApp(ApplicationMessages messages) {
    requestInstallation(() -> showInstructions(messages));
  }

  private static native void requestInstallation(Command fallback) /*-{
    var unavailable = $entry(function () {
      fallback.@com.google.gwt.user.client.Command::execute()();
    });
    if ($wnd.spMobilePwa) $wnd.spMobilePwa.prompt(unavailable);
    else unavailable();
  }-*/;

  private static void showInstructions(ApplicationMessages messages) {
    if (!isSecureContext()) {
      Window.alert(messages.installationRequiresHttps());
    } else if (isIos()) {
      Window.alert(isSafari() ? messages.installationIos() : messages.installationOpenSafari());
    } else {
      Window.alert(messages.installationBrowserMenu());
    }
  }

  private static native boolean isSecureContext() /*-{
    return $wnd.spMobilePwa ? $wnd.spMobilePwa.isSecureContext() : $wnd.isSecureContext === true;
  }-*/;

  private static native boolean isIos() /*-{
    return !!($wnd.spMobilePwa && $wnd.spMobilePwa.isIos());
  }-*/;

  private static native boolean isSafari() /*-{
    return !!($wnd.spMobilePwa && $wnd.spMobilePwa.isSafari());
  }-*/;
}
