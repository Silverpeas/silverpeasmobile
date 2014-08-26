package com.silverpeas.mobile.client.apps.documents.events.app;


public class DocumentsLoadCommentsEvent extends AbstractDocumentsAppEvent {

	private String pubId;

	public DocumentsLoadCommentsEvent(String pubId) {
		super();
		this.pubId = pubId;
	}

	@Override
	protected void dispatch(DocumentsAppEventHandler handler) {
		handler.loadComments(this);
	}

	public String getPubId() {
		return pubId;
	}
}
