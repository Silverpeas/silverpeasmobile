package com.silverpeas.mobile.client.common;

import com.google.gwt.user.client.Window;
import com.gwtmobile.phonegap.client.Notification.Callback;
import com.gwtmobile.ui.client.utils.Utils;


public class Notification {

	public static void activityStart() {
		if (isMobil()) {
			com.gwtmobile.phonegap.client.Notification.activityStart();
		}
	}

    public static void activityStop() {
    	if (isMobil()) {
    		com.gwtmobile.phonegap.client.Notification.activityStop();
    	}
    }
    
    public static void alert(String message, Callback callback, String title, String buttonLabel) {
    	if (isMobil()) {
    		com.gwtmobile.phonegap.client.Notification.alert(message, callback, title, buttonLabel);
    	} else {
    		Window.alert(message);
    	}
    }
    
    public static void progressStart(String title, String message) {
    	if (isMobil()) {
    		com.gwtmobile.phonegap.client.Notification.progressStart(title, message);
    	}
    }
    
    public static void progressStop() {
    	if (isMobil()) {
    		com.gwtmobile.phonegap.client.Notification.progressStop();
    	}
    }
    
    public static void progressValue(int value) {
    	if (isMobil()) {
    		com.gwtmobile.phonegap.client.Notification.progressValue(value);
    	}
    }
    
    private static boolean isMobil() {
    	return Utils.isAndroid() || Utils.isIOS();
    }
}
