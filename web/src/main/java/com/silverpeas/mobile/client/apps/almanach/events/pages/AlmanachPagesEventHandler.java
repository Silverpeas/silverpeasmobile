package com.silverpeas.mobile.client.apps.almanach.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface AlmanachPagesEventHandler extends EventHandler{
	public void onLoadEventDetailDTOLoaded(LoadEventDetailDTOEvent event);
	public void onAlmanachLoaded(AlmanachLoadedEvent event);
}
