package com.silverpeas.mobile.client.apps.documents.events.pages.navigation;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractGedNavigationPagesEvent extends GwtEvent<GedNavigationPagesEventHandler> {

	public static Type<GedNavigationPagesEventHandler> TYPE = new Type<GedNavigationPagesEventHandler>();
	
	public AbstractGedNavigationPagesEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GedNavigationPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
