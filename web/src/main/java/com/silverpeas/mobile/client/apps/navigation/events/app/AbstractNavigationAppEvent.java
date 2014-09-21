package com.silverpeas.mobile.client.apps.navigation.events.app;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractNavigationAppEvent extends GwtEvent<NavigationAppEventHandler> {

	public static Type<NavigationAppEventHandler> TYPE = new Type<NavigationAppEventHandler>();
	
	public AbstractNavigationAppEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NavigationAppEventHandler> getAssociatedType() {
		return TYPE;
	}
}
