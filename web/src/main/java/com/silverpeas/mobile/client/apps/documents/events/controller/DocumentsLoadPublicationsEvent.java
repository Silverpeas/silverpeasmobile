package com.silverpeas.mobile.client.apps.documents.events.controller;


public class DocumentsLoadPublicationsEvent extends AbstractDocumentsControllerEvent {
	
	private String topicId, instanceId;
	
	public DocumentsLoadPublicationsEvent(String instanceId, String topicId) {
		super();
		this.topicId = topicId;
		this.instanceId = instanceId;
	}

	@Override
	protected void dispatch(DocumentsControllerEventHandler handler) {
		handler.loadPublications(this);
	}

	public String getTopicId() {
		return topicId;
	}

	public String getInstanceId() {
		return instanceId;
	}
}
