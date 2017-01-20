package com.silverpeas.mobile.client.apps.news;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.silverpeas.mobile.client.apps.news.events.app.AbstractNewsAppEvent;
import com.silverpeas.mobile.client.apps.news.events.app.NewsAppEventHandler;
import com.silverpeas.mobile.client.apps.news.events.app.NewsLoadEvent;
import com.silverpeas.mobile.client.apps.news.events.pages.NewsLoadedEvent;
import com.silverpeas.mobile.client.apps.news.pages.NewsPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.util.List;

public class NewsApp extends App implements NewsAppEventHandler {

  private static NewsApp instance = null;

  public NewsApp(){
    super();
    EventBus.getInstance().addHandler(AbstractNewsAppEvent.TYPE, this);
  }

  @Override
  public void startAsWidget(SimplePanel container){
    container.add(new NewsPage());
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractNewsAppEvent.TYPE, this);
    super.stop();
  }

  @Override
  public void loadNews(final NewsLoadEvent event) {
    Notification.activityStart();
    ServicesLocator.getServiceNews().loadNews(new AsyncCallback<List<NewsDTO>>() {
      @Override
      public void onFailure(final Throwable caught) {
        EventBus.getInstance().fireEvent(new ErrorEvent(caught));
      }

      @Override
      public void onSuccess(final List<NewsDTO> news) {
        EventBus.getInstance().fireEvent(new NewsLoadedEvent(news));
      }
    });
  }

  public static NewsApp getInstance() {
    if (instance == null) {
      instance = new NewsApp();
    }
    return instance;
  }
}
