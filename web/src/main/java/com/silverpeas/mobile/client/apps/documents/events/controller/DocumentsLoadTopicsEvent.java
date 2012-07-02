package com.silverpeas.mobile.client.apps.documents.events.controller;


public class DocumentsLoadTopicsEvent extends AbstractDocumentsControllerEvent {
	
	private String rootTopicId, instanceId;
	
	public DocumentsLoadTopicsEvent(String instanceId, String rootTopicId) {
		super();
		this.rootTopicId = rootTopicId;
		this.instanceId = instanceId;
	}

	@Override
	protected void dispatch(DocumentsControllerEventHandler handler) {
		handler.loadTopics(this);
	}

	public String getRootTopicId() {
		return rootTopicId;
	}

	public String getInstanceId() {
		return instanceId;
	}
}
