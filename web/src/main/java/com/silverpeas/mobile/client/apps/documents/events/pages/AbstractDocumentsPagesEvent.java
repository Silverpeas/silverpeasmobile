package com.silverpeas.mobile.client.apps.documents.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractDocumentsPagesEvent extends GwtEvent<DocumentsPagesEventHandler> {

	public static Type<DocumentsPagesEventHandler> TYPE = new Type<DocumentsPagesEventHandler>();
	
	public AbstractDocumentsPagesEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DocumentsPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
