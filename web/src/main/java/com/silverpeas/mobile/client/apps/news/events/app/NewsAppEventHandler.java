package com.silverpeas.mobile.client.apps.news.events.app;

import com.google.gwt.event.shared.EventHandler;

public interface NewsAppEventHandler extends EventHandler{
  void loadNews(NewsLoadEvent event);
}
