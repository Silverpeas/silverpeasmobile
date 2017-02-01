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
  public void startAsWidget(SimplePanel container){
    EventBus.getInstance().addHandler(AbstractNewsAppEvent.TYPE, this);
    super.startAsWidget(container);
    container.add(new NewsPage());
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
    Notification.activityStart();

    final SpMobileRequestBuilder rb = new SpMobileRequestBuilder(RequestBuilder.GET, "/silverpeas/services/fragments/news/last/" + SpMobil.getConfiguration().getNewsNumber());

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

  public void updateDisplay() {
    loadNews(new NewsLoadEvent());
  }
}
