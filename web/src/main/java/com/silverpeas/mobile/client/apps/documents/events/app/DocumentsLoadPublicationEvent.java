package com.silverpeas.mobile.client.apps.documents.events.app;


public class DocumentsLoadPublicationEvent extends AbstractDocumentsAppEvent {
	
	private String pubId;
	
	public DocumentsLoadPublicationEvent(String pubId) {
		super();
		this.pubId = pubId;
	}

	@Override
	protected void dispatch(DocumentsAppEventHandler handler) {
		handler.loadPublication(this);
	}

	public String getPubId() {
		return pubId;
	}
}
