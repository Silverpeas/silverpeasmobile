package com.silverpeas.mobile.client.apps.dashboard.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface DashboardPagesEventHandler extends EventHandler{
	void onDashboardLoaded(DashboardLoadedEvent event);
}
