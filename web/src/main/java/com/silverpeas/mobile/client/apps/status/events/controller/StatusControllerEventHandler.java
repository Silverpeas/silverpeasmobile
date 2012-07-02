package com.silverpeas.mobile.client.apps.status.events.controller;

import com.google.gwt.event.shared.EventHandler;

public interface StatusControllerEventHandler extends EventHandler{
	void loadStatus(StatusLoadEvent event);
	void postStatus(StatusPostEvent event);
}
