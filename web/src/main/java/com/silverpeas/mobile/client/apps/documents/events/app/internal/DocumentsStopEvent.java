package com.silverpeas.mobile.client.apps.documents.events.app.internal;

public class DocumentsStopEvent extends AbstractDocumentsEvent {
	
	public DocumentsStopEvent() {
		super();
	}

	@Override
	protected void dispatch(DocumentsEventHandler handler) {
		handler.onStop(this);
	}
}
