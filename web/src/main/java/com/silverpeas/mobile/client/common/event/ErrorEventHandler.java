package com.silverpeas.mobile.client.common.event;

import com.google.gwt.event.shared.EventHandler;

public interface ErrorEventHandler extends EventHandler {
	void onError(ExceptionEvent event);
}