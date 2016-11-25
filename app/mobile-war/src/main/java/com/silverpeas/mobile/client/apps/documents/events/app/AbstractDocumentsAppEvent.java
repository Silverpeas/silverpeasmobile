package com.silverpeas.mobile.client.apps.documents.events.app;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractDocumentsAppEvent extends GwtEvent<DocumentsAppEventHandler> {

	public static Type<DocumentsAppEventHandler> TYPE = new Type<DocumentsAppEventHandler>();
	
	public AbstractDocumentsAppEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DocumentsAppEventHandler> getAssociatedType() {
		return TYPE;
	}
}
