package com.silverpeas.mobile.client.apps.status.events.controller;

import com.google.gwt.event.shared.EventHandler;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPostEvent;

public interface StatusControllerEventHandler extends EventHandler{
	void loadStatus(StatusLoadEvent event);
	void postStatus(StatusPostEvent event);
}
