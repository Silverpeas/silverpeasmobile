package com.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtmobile.phonegap.client.Notification.Callback;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;
import com.silverpeas.mobile.client.resources.ApplicationResources;


public class Notification {
	
	private static ApplicationResources res =  GWT.create(ApplicationResources.class);

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
    		DialogBox popup = new DialogBox(true);    		
    		popup.setText(title);
    		popup.add(new HTML("<SPAN>"+message+"</SPAN>"));
    		popup.setGlassEnabled(true);
    		popup.setWidth(Window.getClientWidth() - 30 +"px");
    		popup.setHeight(Window.getClientWidth() /2 + "px");    		
    		popup.setStylePrimaryName(res.css().popup());    		
    		popup.center();
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
