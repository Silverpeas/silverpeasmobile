package com.silverpeas.mobile.client.apps.documents.events.controller;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractDocumentsControllerEvent extends GwtEvent<DocumentsControllerEventHandler> {

	public static Type<DocumentsControllerEventHandler> TYPE = new Type<DocumentsControllerEventHandler>();
	
	public AbstractDocumentsControllerEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DocumentsControllerEventHandler> getAssociatedType() {
		return TYPE;
	}
}
