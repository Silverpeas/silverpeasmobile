package com.silverpeas.mobile.client.apps.news.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface NewsPagesEventHandler extends EventHandler{
	void onNewsLoad(NewsLoadedEvent event);
}
