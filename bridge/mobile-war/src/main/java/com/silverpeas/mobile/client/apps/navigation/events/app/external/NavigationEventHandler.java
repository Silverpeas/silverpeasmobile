package com.silverpeas.mobile.client.apps.navigation.events.app.external;

import com.google.gwt.event.shared.EventHandler;

public interface NavigationEventHandler extends EventHandler {
	void appInstanceChanged(NavigationAppInstanceChangedEvent event);
}