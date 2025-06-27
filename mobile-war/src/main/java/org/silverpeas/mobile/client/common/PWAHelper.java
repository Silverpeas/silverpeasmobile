package org.silverpeas.mobile.client.common;

public class PWAHelper {
    public static native void detectAppInstallation() /*-{

        $wnd.addEventListener("beforeinstallprompt", function(event)  {
            event.preventDefault();
            $wnd.installPrompt = event;
        });

    }-*/;

    public static native boolean isInstalledApp() /*-{
        return !$wnd.installPrompt;
    }-*/;

    public static native void installApp() /*-{
        $wnd.installPrompt.prompt();
    }-*/;



}
