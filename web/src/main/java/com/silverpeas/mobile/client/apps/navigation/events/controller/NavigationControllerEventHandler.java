package com.silverpeas.mobile.client.apps.navigation.events.controller;

import com.google.gwt.event.shared.EventHandler;

public interface NavigationControllerEventHandler extends EventHandler {
	void loadSpacesAndApps(LoadSpacesAndAppsEvent event);
}