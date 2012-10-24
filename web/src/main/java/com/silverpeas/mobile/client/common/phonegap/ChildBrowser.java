package com.silverpeas.mobile.client.common.phonegap;

import com.gwtmobile.phonegap.client.Utils;

public class ChildBrowser {
	private static EventHandler eventHandler;

	static {
		if (Utils.isIOS()) {
			install();
		}
		initCallbacks();
	}
    		  	
    private native static void install() /*-{
        $wnd.ChildBrowser.install();
    }-*/;
    
    private native static void initCallbacks() /*-{
        $wnd.plugins.childBrowser.onLocationChange = function(loc) {
            @com.silverpeas.mobile.client.common.phonegap.ChildBrowser::onLocationChange(Ljava/lang/String;)(loc);
        };
        $wnd.plugins.childBrowser.onClose = function() {
            @com.silverpeas.mobile.client.common.phonegap.ChildBrowser::onClose()();
        };
        $wnd.plugins.childBrowser.onOpenExternal = function() {
            @com.silverpeas.mobile.client.common.phonegap.ChildBrowser::onOpenExternal()();
        };

    }-*/;
    
    public static native void openExternal(String targetUrl, boolean usecordova) /*-{		
		$wnd.plugins.childBrowser.openExternal(targetUrl, usecordova);
	}-*/;

    public static native void showWebPage(String url) /*-{
        $wnd.plugins.childBrowser.showWebPage(url);
    }-*/;

    public static native void close() /*-{
        $wnd.plugins.childBrowser.close();
    }-*/;


    private static void onClose() {
    	if (eventHandler != null) {
    		eventHandler.onClose();
    	}
    }

    private static void onOpenExternal() {
    	if (eventHandler != null) {
    		eventHandler.onOpenExternal();
    	}
    }

    private static void onLocationChange(String url) {
    	if (eventHandler != null) {
    		eventHandler.onLocationChange(url);
    	}
    }

    public static interface EventHandler {
        void onClose();
        void onOpenExternal();
        void onLocationChange(String url);
    }

    public static void setEventHandler(EventHandler eventHandler) {
    	ChildBrowser.eventHandler = eventHandler;
    }
    
    public static EventHandler getEventHandler() {
    	return ChildBrowser.eventHandler; 
    }

}
