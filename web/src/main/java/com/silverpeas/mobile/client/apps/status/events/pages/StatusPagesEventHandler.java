package com.silverpeas.mobile.client.apps.status.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface StatusPagesEventHandler extends EventHandler{
	void onStatusLoaded(StatusLoadedEvent event);
}
