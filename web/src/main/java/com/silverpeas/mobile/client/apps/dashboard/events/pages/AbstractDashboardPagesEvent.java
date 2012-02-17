package com.silverpeas.mobile.client.apps.dashboard.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractDashboardPagesEvent extends GwtEvent<DashboardPagesEventHandler>{
	
	public static Type<DashboardPagesEventHandler> TYPE = new Type<DashboardPagesEventHandler>();
	
	public AbstractDashboardPagesEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DashboardPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
