package com.silverpeas.mobile.client.apps.documents.events.controller;

import com.silverpeas.mobile.shared.dto.documents.TopicDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class DocumentsSaveSettingsEvent extends AbstractDocumentsControllerEvent {
	
	private ApplicationInstanceDTO instance;
	private TopicDTO topic;
	
	public DocumentsSaveSettingsEvent(ApplicationInstanceDTO instance, TopicDTO topic) {
		super();
		this.instance = instance;
		this.topic = topic;
	}

	@Override
	protected void dispatch(DocumentsControllerEventHandler handler) {
		handler.saveSettings(this);
	}

	public ApplicationInstanceDTO getInstance() {
		return instance;
	}

	public TopicDTO getTopic() {
		return topic;
	}
}
