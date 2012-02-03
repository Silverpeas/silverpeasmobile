package com.silverpeas.mobile.client.apps.navigation.events.app.internal;


public class NavigationStopEvent extends AbstractNavigationEvent {
	
	public NavigationStopEvent() {
		super();
	}

	@Override
	protected void dispatch(NavigationEventHandler handler) {
		handler.onStop(this);
	}
}
