package com.silverpeas.mobile.client.apps.dashboard.events.app.internal;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractDashboardEvent extends GwtEvent<DashboardEventHandler>{
	
	public static Type<DashboardEventHandler> TYPE = new Type<DashboardEventHandler>();
	
	public AbstractDashboardEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DashboardEventHandler> getAssociatedType() {
		return TYPE;
	}
}
