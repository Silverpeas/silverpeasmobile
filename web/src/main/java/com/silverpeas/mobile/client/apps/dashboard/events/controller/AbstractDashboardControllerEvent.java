package com.silverpeas.mobile.client.apps.dashboard.events.controller;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractDashboardControllerEvent extends GwtEvent<DashboardControllerEventHandler>{

	public static Type<DashboardControllerEventHandler> TYPE = new Type<DashboardControllerEventHandler>();
	
	public AbstractDashboardControllerEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DashboardControllerEventHandler> getAssociatedType() {
		return TYPE;
	}
}
