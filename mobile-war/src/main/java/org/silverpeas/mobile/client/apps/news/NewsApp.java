/*
 * Copyright (C) 2000 - 2019 Silverpeas
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.apps.news;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.news.events.app.AbstractNewsAppEvent;
import org.silverpeas.mobile.client.apps.news.events.app.NewsAppEventHandler;
import org.silverpeas.mobile.client.apps.news.events.app.NewsLoadEvent;
import org.silverpeas.mobile.client.apps.news.events.pages.NewsLoadedEvent;
import org.silverpeas.mobile.client.apps.news.pages.NewsPage;
import org.silverpeas.mobile.client.apps.news.resources.NewsMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

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
