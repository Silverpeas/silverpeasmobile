package com.silverpeas.mobile.client.apps.news;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
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
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.common.network.RequestCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.network.SpMobileRequestBuilder;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
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

    final SpMobileRequestBuilder rb = new SpMobileRequestBuilder(RequestBuilder.GET, "/silverpeas/services/fragments/news/last/5");

    final String key = "lastNews";
    Command offlineAction = new Command() {
      @Override
      public void execute() {
        String result = LocalStorageHelper.load(key, String.class);
        if (result == null) {
          result = new String();
        }
        EventBus.getInstance().fireEvent(new NewsLoadedEvent(result));
      }
    };

    RequestCallbackOnlineOrOffline action = new RequestCallbackOnlineOrOffline(offlineAction) {
      @Override
      public void attempt() {
        try {
          rb.sendRequest(null, this);
        } catch (RequestException caught) {
          this.onError(caught);
        }
      }

      @Override
      public void onResponseReceived(Request request, Response response) {
        super.onResponseReceived(request, response);
        LocalStorageHelper.store(key, String.class, response.getText());
        EventBus.getInstance().fireEvent(new NewsLoadedEvent(response.getText()));
      }
    };
    action.attempt();
  }

  public static NewsApp getInstance() {
    if (instance == null) {
      instance = new NewsApp();
    }
    return instance;
  }
}
