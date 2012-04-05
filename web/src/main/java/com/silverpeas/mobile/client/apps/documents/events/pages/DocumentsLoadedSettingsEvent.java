package com.silverpeas.mobile.client.apps.documents.events.pages;

import com.silverpeas.mobile.shared.dto.documents.TopicDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class DocumentsLoadedSettingsEvent extends AbstractDocumentsPagesEvent {

	private ApplicationInstanceDTO instance;
	private TopicDTO topic;
	
	public DocumentsLoadedSettingsEvent(ApplicationInstanceDTO instance, TopicDTO topic) {
		super();
		this.instance = instance;
		this.topic = topic;
	}

	@Override
	protected void dispatch(DocumentsPagesEventHandler handler) {
		handler.onLoadedSettings(this);
	}

	public ApplicationInstanceDTO getInstance() {
		return instance;
	}

	public TopicDTO getTopic() {
		return topic;
	}	
}
