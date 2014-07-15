package com.silverpeas.mobile.client.apps.documents.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface DocumentsPagesEventHandler extends EventHandler {
	void onLoadedSettings(DocumentsLoadedSettingsEvent event);
}