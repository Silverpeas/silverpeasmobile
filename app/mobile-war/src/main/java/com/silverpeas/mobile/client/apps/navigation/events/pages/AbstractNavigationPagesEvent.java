package com.silverpeas.mobile.client.apps.navigation.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractNavigationPagesEvent extends GwtEvent<NavigationPagesEventHandler> {

	public static Type<NavigationPagesEventHandler> TYPE = new Type<NavigationPagesEventHandler>();
	
	public AbstractNavigationPagesEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NavigationPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
