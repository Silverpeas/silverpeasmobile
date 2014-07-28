package com.silverpeas.mobile.client.common;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;


public class Notification {
		

	public static void activityStart() {
		RootPanel.getBodyElement().getStyle().setProperty("cursor", "wait");
	}

    public static void activityStop() {   		
   		RootPanel.getBodyElement().getStyle().setProperty("cursor", "default");
    }
    
    public static void alert(String message, Callback<Boolean, Boolean> callback, String title, String buttonLabel) {
		DialogBox popup = new DialogBox(true);    		
		popup.setText(title);
		popup.add(new HTML("<SPAN>"+message+"</SPAN>"));
		popup.setGlassEnabled(true);
		popup.setWidth(Window.getClientWidth() - 30 +"px");
		popup.setHeight(Window.getClientWidth() /2 + "px");
		popup.center();    	
    }
    
    public static void confirm(String message, Callback<Boolean, Boolean> callback, String buttonsLabels) {
    	//TODO : implementation
    }
    
    public static void progressStart(String title, String message) {
    	RootPanel.getBodyElement().getStyle().setProperty("cursor", "progress");    	
    }
    
    public static void progressStop() {   		
   		RootPanel.getBodyElement().getStyle().setProperty("cursor", "default");
    }
    
    public static void progressValue(int value) {
    	//TODO : implementation
    }
}
