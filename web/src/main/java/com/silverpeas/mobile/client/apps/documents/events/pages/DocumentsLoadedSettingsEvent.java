package com.silverpeas.mobile.client.apps.documents.events.pages;

import com.silverpeas.mobile.client.apps.documents.persistances.DocumentsSettings;

public class DocumentsLoadedSettingsEvent extends AbstractDocumentsPagesEvent {

	private DocumentsSettings settings;
	
	public DocumentsLoadedSettingsEvent(DocumentsSettings settings) {
		super();
		this.settings = settings;
	}

	@Override
	protected void dispatch(DocumentsPagesEventHandler handler) {
		handler.onLoadedSettings(this);
	}

	public DocumentsSettings getSettings() {
		return settings;
	}
}
