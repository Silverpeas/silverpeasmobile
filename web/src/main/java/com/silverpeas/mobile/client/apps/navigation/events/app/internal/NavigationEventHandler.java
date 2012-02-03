package com.silverpeas.mobile.client.apps.navigation.events.app.internal;

import com.google.gwt.event.shared.EventHandler;

public interface NavigationEventHandler extends EventHandler {
	void onStop(NavigationStopEvent event);
}