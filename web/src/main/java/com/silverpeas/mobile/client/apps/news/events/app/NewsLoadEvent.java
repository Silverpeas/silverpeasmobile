package com.silverpeas.mobile.client.apps.news.events.app;

public class NewsLoadEvent extends AbstractNewsAppEvent {

  public NewsLoadEvent(){
    super();
  }

  @Override
  protected void dispatch(NewsAppEventHandler handler) {
    handler.loadNews(this);
  }
}
