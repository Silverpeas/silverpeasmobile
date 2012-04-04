package com.silverpeas.mobile.client.apps.documents.events.controller;


public class DocumentsLoadTopicsEvent extends AbstractDocumentsControllerEvent {
	
	private String rootTopicId;
	
	public DocumentsLoadTopicsEvent(String rootTopicId) {
		super();
		this.rootTopicId = rootTopicId;
	}

	@Override
	protected void dispatch(DocumentsControllerEventHandler handler) {
		handler.loadTopics(this);
	}

	public String getRootTopicId() {
		return rootTopicId;
	}
}
