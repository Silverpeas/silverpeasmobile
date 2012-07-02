package com.silverpeas.mobile.client.apps.documents.events.controller;


public class DocumentsLoadPublicationEvent extends AbstractDocumentsControllerEvent {
	
	private String pubId;
	
	public DocumentsLoadPublicationEvent(String pubId) {
		super();
		this.pubId = pubId;
	}

	@Override
	protected void dispatch(DocumentsControllerEventHandler handler) {
		handler.loadPublication(this);
	}

	public String getPubId() {
		return pubId;
	}
}
