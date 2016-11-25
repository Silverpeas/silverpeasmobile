package com.silverpeas.mobile.client.components.base.events;

import com.google.gwt.event.shared.EventHandler;

public interface ScrollEventHandler extends EventHandler{
	void onScrollEndPage(EndPageEvent event);
}
