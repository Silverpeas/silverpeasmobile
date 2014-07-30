package com.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Composite;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.navigation.PageHistory;

public class PageContent extends Composite implements View, NativePreviewHandler {

	private App app;
	protected boolean clicked = false;
	protected String pageTitle = "Silverpeas";

	public PageContent() {
		super();
		Event.addNativePreviewHandler(this);
	}
	
	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	
	public void show() {
		PageHistory.getInstance().goTo(this);
	}
	
	public boolean isVisible() { 
		return PageHistory.getInstance().isVisible(this);
	}
	
	public void back() {
		PageHistory.getInstance().back();
	}

	public void clickGesture(Command call) {
		if (!clicked) {
			clicked = true;
			call.execute();			
			Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
				@Override
				public boolean execute() {
					clicked = false;					
					return false;
				}}, 400);	
		}		
	}

	@Override
	public void stop() {
		if (app != null) app.stop();
	}

	@Override
	public void onPreviewNativeEvent(NativePreviewEvent event) {		
		if (event.getTypeInt() == Event.ONCLICK) {						
	        Element target = event.getNativeEvent().getEventTarget().cast();	        
	        while(target.getParentElement() != null) {	        	
	        	if (target.getId().equals("silverpeas-navmenu-panel") || target.getId().equals("menu")) {
					return;
				}
	        	target = target.getParentElement();
	        }			
			SpMobil.mainPage.closeMenu();
		}
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}
}
