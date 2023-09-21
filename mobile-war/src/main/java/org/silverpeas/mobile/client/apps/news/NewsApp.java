/*
 * Copyright (C) 2000 - 2022 Silverpeas
 *
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
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
import org.fusesource.restygwt.client.Method;
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
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

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
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<NewsDTO>>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceNews().getNews(instance.getId(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<NewsDTO> news) {
        super.onSuccess(method, news);
        EventBus.getInstance().fireEvent(new NewsLoadedEvent(news));
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

  private void loadAppInstance(final ContentDTO content) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<ApplicationInstanceDTO>() {

      @Override
      public void attempt() {
        ServicesLocator.getServiceNavigation()
                .getApp(content.getInstanceId(), content.getId(), content.getType(), this);
      }

      @Override
      public void onSuccess(final Method method,
                            final ApplicationInstanceDTO app) {
        super.onSuccess(method, app);
        setApplicationInstance(app);
        appInstanceChanged(new NavigationAppInstanceChangedEvent(app));
      }
    };
    action.attempt();
  }


  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals("Component") && event.getContent().getInstanceId().startsWith(Apps.quickinfo.name())) {
      loadAppInstance(event.getContent());
    } else {
      // actually manage by document app
    }
  }
}
