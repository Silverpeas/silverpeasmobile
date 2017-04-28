package com.silverpeas.mobile.client.apps.news;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import com.silverpeas.mobile.client.apps.news.events.app.AbstractNewsAppEvent;
import com.silverpeas.mobile.client.apps.news.events.app.NewsAppEventHandler;
import com.silverpeas.mobile.client.apps.news.events.app.NewsLoadEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.apps.news.events.pages.NewsLoadedEvent;
import com.silverpeas.mobile.client.apps.news.pages.NewsPage;
import com.silverpeas.mobile.client.apps.news.resources.NewsMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.Apps;
import com.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.util.ArrayList;
import java.util.List;

public class NewsApp extends App implements NewsAppEventHandler, NavigationEventHandler {

  private NewsMessages msg;
  private ApplicationInstanceDTO instance;

  public NewsApp(){
    super();
    msg = GWT.create(NewsMessages.class);
    EventBus.getInstance().addHandler(AbstractNewsAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  public void start(){
    // always start
  }

  @Override
  public void stop() {
    // nevers stop
  }

  @Override
  public void loadNews(final NewsLoadEvent event) {
    final String key = "news_" + instance.getId();
    Command offlineAction = new Command() {
      @Override
      public void execute() {
        List<NewsDTO> result = LocalStorageHelper.load(key, List.class);
        if (result == null) {
          result = new ArrayList<NewsDTO>();
        }
        EventBus.getInstance().fireEvent(new NewsLoadedEvent(result));
      }
    };

    AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<List<NewsDTO>>(offlineAction) {
      @Override
      public void attempt() {
        ServicesLocator.getServiceNews().getNews(instance.getId(), this);
      }

      @Override
      public void onSuccess(List<NewsDTO> result) {
        super.onSuccess(result);
        LocalStorageHelper.store(key, List.class, result);
        EventBus.getInstance().fireEvent(new NewsLoadedEvent(result));
      }
    };
    action.attempt();
  }

  public void updateDisplay() {
    loadNews(new NewsLoadEvent());
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
    if (event.getInstance().getType().equals(Apps.quickinfo.name())) {
      this.instance = event.getInstance();
      NewsPage page = new NewsPage();
      page.setPageTitle(event.getInstance().getLabel());
      page.show();

    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    // actually manage by document app
  }
}
