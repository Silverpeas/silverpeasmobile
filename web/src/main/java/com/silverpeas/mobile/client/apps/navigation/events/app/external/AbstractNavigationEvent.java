package com.silverpeas.mobile.client.apps.navigation.events.app.external;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractNavigationEvent extends GwtEvent<NavigationEventHandler> {

	public static Type<NavigationEventHandler> TYPE = new Type<NavigationEventHandler>();
	
	public AbstractNavigationEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NavigationEventHandler> getAssociatedType() {
		return TYPE;
	}
}