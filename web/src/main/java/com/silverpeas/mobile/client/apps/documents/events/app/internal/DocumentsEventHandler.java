package com.silverpeas.mobile.client.apps.documents.events.app.internal;

import com.google.gwt.event.shared.EventHandler;

public interface DocumentsEventHandler extends EventHandler {
	void onStop(DocumentsStopEvent event);
}