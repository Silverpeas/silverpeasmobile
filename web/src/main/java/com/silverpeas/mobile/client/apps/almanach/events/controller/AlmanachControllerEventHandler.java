package com.silverpeas.mobile.client.apps.almanach.events.controller;

import com.google.gwt.event.shared.EventHandler;

public interface AlmanachControllerEventHandler extends EventHandler{
	void loadAlmanach(AlmanachLoadEvent event);
}
