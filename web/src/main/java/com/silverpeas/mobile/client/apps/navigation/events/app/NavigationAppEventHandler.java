package com.silverpeas.mobile.client.apps.navigation.events.app;

import com.google.gwt.event.shared.EventHandler;

public interface NavigationAppEventHandler extends EventHandler {
	void loadSpacesAndApps(LoadSpacesAndAppsEvent event);
}