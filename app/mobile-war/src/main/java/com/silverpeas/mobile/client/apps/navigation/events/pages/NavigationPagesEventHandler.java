package com.silverpeas.mobile.client.apps.navigation.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface NavigationPagesEventHandler extends EventHandler {
	void homePageLoaded(HomePageLoadedEvent event);
	void clickItem(ClickItemEvent event);
}