package com.silverpeas.mobile.client.apps.news.events.app;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractNewsAppEvent extends GwtEvent<NewsAppEventHandler>{

  public static Type<NewsAppEventHandler> TYPE = new Type<NewsAppEventHandler>();

  public AbstractNewsAppEvent(){
  }

  @Override
  public Type<NewsAppEventHandler> getAssociatedType() {
    return TYPE;
  }
}
