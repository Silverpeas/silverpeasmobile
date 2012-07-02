package com.silverpeas.mobile.client.apps.dashboard.events.app.internal;

public class DashboardStopEvent extends AbstractDashboardEvent{

	public DashboardStopEvent(){
		super();
	}
	
	@Override
	protected void dispatch(DashboardEventHandler handler) {
		handler.onStop(this);
	}
}
