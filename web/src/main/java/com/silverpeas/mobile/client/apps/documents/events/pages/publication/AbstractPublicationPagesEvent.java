package com.silverpeas.mobile.client.apps.documents.events.pages.publication;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractPublicationPagesEvent extends GwtEvent<PublicationNavigationPagesEventHandler> {

	public static Type<PublicationNavigationPagesEventHandler> TYPE = new Type<PublicationNavigationPagesEventHandler>();
	
	public AbstractPublicationPagesEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PublicationNavigationPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
