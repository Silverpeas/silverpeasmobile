package com.silverpeas.mobile.client.apps.documents.events.pages.publication;

import com.google.gwt.event.shared.EventHandler;

public interface PublicationNavigationPagesEventHandler extends EventHandler {
	void onLoadedPublication(PublicationLoadedEvent event);	
}