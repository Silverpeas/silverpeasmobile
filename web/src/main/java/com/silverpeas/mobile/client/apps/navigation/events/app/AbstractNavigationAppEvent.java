package com.silverpeas.mobile.client.apps.navigation.events.controller;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractNavigationControllerEvent extends GwtEvent<NavigationControllerEventHandler> {

	public static Type<NavigationControllerEventHandler> TYPE = new Type<NavigationControllerEventHandler>();
	
	public AbstractNavigationControllerEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NavigationControllerEventHandler> getAssociatedType() {
		return TYPE;
	}
}
