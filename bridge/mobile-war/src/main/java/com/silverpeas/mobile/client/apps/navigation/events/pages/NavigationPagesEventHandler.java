package com.silverpeas.mobile.client.apps.navigation.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface NavigationPagesEventHandler extends EventHandler {
	void spacesAndAppsLoaded(SpacesAndAppsLoadedEvent event);
	void clickItem(ClickItemEvent event);
}