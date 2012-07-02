package com.silverpeas.mobile.client.apps.documents.events.app.internal;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractDocumentsEvent extends GwtEvent<DocumentsEventHandler> {

	public static Type<DocumentsEventHandler> TYPE = new Type<DocumentsEventHandler>();
	
	public AbstractDocumentsEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DocumentsEventHandler> getAssociatedType() {
		return TYPE;
	}
}
