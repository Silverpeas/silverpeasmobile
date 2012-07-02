package com.silverpeas.mobile.client.apps.documents.events.controller;

import com.google.gwt.event.shared.EventHandler;

public interface DocumentsControllerEventHandler extends EventHandler {
	void loadSettings(DocumentsLoadSettingsEvent event);
	void saveSettings(DocumentsSaveSettingsEvent event);
	void loadTopics(DocumentsLoadTopicsEvent event);
	void loadPublications(DocumentsLoadPublicationsEvent event);
	void loadPublication(DocumentsLoadPublicationEvent event);
}