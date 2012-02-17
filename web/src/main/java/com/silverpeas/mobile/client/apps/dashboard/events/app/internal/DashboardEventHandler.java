package com.silverpeas.mobile.client.apps.dashboard.events.app.internal;

import com.google.gwt.event.shared.EventHandler;

public interface DashboardEventHandler extends EventHandler{
	void onStop(DashboardStopEvent event);
}
