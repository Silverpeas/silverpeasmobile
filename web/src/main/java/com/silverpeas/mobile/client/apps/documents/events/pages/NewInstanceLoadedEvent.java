package com.silverpeas.mobile.client.apps.documents.events.pages;

import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class NewInstanceLoadedEvent extends AbstractDocumentsPagesEvent {

	private ApplicationInstanceDTO instance;
	
	public NewInstanceLoadedEvent(ApplicationInstanceDTO instance) {
		super();
		this.instance = instance;
	}

	@Override
	protected void dispatch(DocumentsPagesEventHandler handler) {
		handler.onNewInstanceLoaded(this);
	}

	public ApplicationInstanceDTO getInstance() {
		return instance;
	}
}
