package com.silverpeas.mobile.client.apps.documents.events.controller;

public class DocumentsLoadSettingsEvent extends AbstractDocumentsControllerEvent {
	
	public DocumentsLoadSettingsEvent() {
		super();
	}

	@Override
	protected void dispatch(DocumentsControllerEventHandler handler) {
		handler.loadSettings(this);
	}
}
