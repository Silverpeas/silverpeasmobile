package com.silverpeas.mobile.client.apps.status.events.app.internal;

import com.google.gwt.event.shared.EventHandler;

public interface StatusEventHandler extends EventHandler{
	void onStop(StatusStopEvent event);
}
