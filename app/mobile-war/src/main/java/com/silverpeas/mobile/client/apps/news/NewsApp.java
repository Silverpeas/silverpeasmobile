package com.silverpeas.mobile.client.apps.news;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.SimplePanel;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.news.events.app.AbstractNewsAppEvent;
import com.silverpeas.mobile.client.apps.news.events.app.NewsAppEventHandler;
import com.silverpeas.mobile.client.apps.news.events.app.NewsLoadEvent;
import com.silverpeas.mobile.client.apps.news.events.pages.NewsLoadedEvent;
import com.silverpeas.mobile.client.apps.news.pages.NewsPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.network.RequestCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.network.SpMobileRequestBuilder;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;

public class NewsApp extends App implements NewsAppEventHandler {


  public NewsApp(){
    super();
  }

  @Override
  public void stop() {
    ((NewsPage) container.getWidget()).stop();
    container.clear();
    EventBus.getInstance().removeHandler(AbstractNewsAppEvent.TYPE, this);
    super.stop();
  }

  @Override
  public void loadNews(final NewsLoadEvent event) {


  }

  public void updateDisplay() {
    loadNews(new NewsLoadEvent());
  }
}
