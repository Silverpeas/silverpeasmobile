package com.silverpeas.mobile.client.apps.documents.events.pages.navigation;

import com.google.gwt.event.shared.EventHandler;

public interface GedNavigationPagesEventHandler extends EventHandler {
	void onLoadedTopics(GedItemsLoadedEvent event);
	void onGedItemClicked(GedItemClickEvent event);
}