package com.silverpeas.mobile.client.apps.documents.events.app;


public class DocumentsLoadGedItemsEvent extends AbstractDocumentsAppEvent {
	
	private String rootTopicId, instanceId;
	
	public DocumentsLoadGedItemsEvent(String instanceId, String rootTopicId) {
		super();
		this.rootTopicId = rootTopicId;
		this.instanceId = instanceId;
	}

	@Override
	protected void dispatch(DocumentsAppEventHandler handler) {
		handler.loadTopics(this);
	}

	public String getRootTopicId() {
		return rootTopicId;
	}

	public String getInstanceId() {
		return instanceId;
	}
}
