package com.silverpeas.mobile.client.apps.almanach.events.app.internal;

import com.google.gwt.event.shared.EventHandler;

public interface AlmanachEventHandler extends EventHandler{
	void onStop(AlmanachStopEvent event);
}
