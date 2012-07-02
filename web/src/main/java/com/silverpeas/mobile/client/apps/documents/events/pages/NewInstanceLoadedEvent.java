package com.silverpeas.mobile.client.apps.documents.events.pages;

import java.util.List;

import com.silverpeas.mobile.shared.dto.gallery.AlbumDTO;
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
