package com.silverpeas.mobile.client.common;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtmobile.phonegap.client.Notification.Callback;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;


public class Notification {

	public static void activityStart() {
		if (MobilUtils.isPhoneGap()) {
			com.gwtmobile.phonegap.client.Notification.activityStart();
		} else {
			DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
		}
	}

    public static void activityStop() {
    	if (MobilUtils.isPhoneGap()) {
    		com.gwtmobile.phonegap.client.Notification.activityStop();
    	} else {
    		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
    	}
    }
    
    public static void alert(String message, Callback callback, String title, String buttonLabel) {
    	if (MobilUtils.isPhoneGap()) {
    		com.gwtmobile.phonegap.client.Notification.alert(message, callback, title, buttonLabel);
    	} else {
    		Window.alert(message);
    	}
    }
    
    public static void progressStart(String title, String message) {
    	if (MobilUtils.isPhoneGap()) {
    		com.gwtmobile.phonegap.client.Notification.progressStart(title, message);
    	} else {
    		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "progress");
    	}
    }
    
    public static void progressStop() {
    	if (MobilUtils.isPhoneGap()) {
    		com.gwtmobile.phonegap.client.Notification.progressStop();
    	} else {
    		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
    	}
    }
    
    public static void progressValue(int value) {
    	if (MobilUtils.isPhoneGap()) {
    		com.gwtmobile.phonegap.client.Notification.progressValue(value);
    	}
    }
}
