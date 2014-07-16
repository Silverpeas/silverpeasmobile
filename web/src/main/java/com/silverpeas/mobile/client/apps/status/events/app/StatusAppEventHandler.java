package com.silverpeas.mobile.client.apps.status.events.app;

import com.google.gwt.event.shared.EventHandler;

public interface StatusAppEventHandler extends EventHandler{	
	void postStatus(StatusPostEvent event);
}
