/*
 * Copyright (C) 2000 - 2024 Silverpeas
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

package org.silverpeas.mobile.client.apps.navigation;

import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationAppEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.LoadSpacesAndAppsEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.navigation.events.pages.HomePageLoadedEvent;
import org.silverpeas.mobile.client.apps.navigation.pages.NavigationPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.ShortCutRouter;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.HomePageDTO;
import org.silverpeas.mobile.shared.dto.navigation.HomePages;
import org.silverpeas.mobile.shared.dto.navigation.SpaceDTO;

public class NavigationApp extends App implements NavigationAppEventHandler,NavigationEventHandler {

    private String title;
    private ApplicationMessages msg;

    public NavigationApp() {
        super();
        msg = GWT.create(ApplicationMessages.class);
        EventBus.getInstance().addHandler(AbstractNavigationAppEvent.TYPE, this);
        EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
    }

    @Override
    public void start() {
        NavigationPage mainPage = new NavigationPage();
        mainPage.setPageTitle(title);
        mainPage.setRootSpaceId(null);
        setMainPage(mainPage);
        mainPage.show();
        // no "super.start(lauchingPage);" this apps is used in another apps
    }

    @Override
    public void stop() {
        // never stop
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void loadSpacesAndApps(final LoadSpacesAndAppsEvent event) {
      //TODO : replace call getSpaceAndApps by getHomePage

      MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<HomePageDTO>() {

        @Override
        public void attempt() {
          ServicesLocator.getServiceNavigation().getHomePageData(event.getRootSpaceId(), SpMobil.getContentZoomLevel(), this);
        }

        @Override
        public void onSuccess(final Method method, final HomePageDTO result) {
          super.onSuccess(method, result);
          EventBus.getInstance().fireEvent(new HomePageLoadedEvent(result));
        }
      };
      action.attempt();
    }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
      if (event.getContent().getType().equals(ContentsTypes.Space.name())) {
          final String id;
          if (event.getContent().getInstanceId() == null || event.getContent().getInstanceId().isEmpty()) {
              id = event.getContent().getId();
          } else {
              id = event.getContent().getInstanceId();
          }
          MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<SpaceDTO>() {
              @Override
              public void attempt() {
                  ServicesLocator.getServiceNavigation().getSpace(id, this);
              }
              @Override
              public void onSuccess(Method method, SpaceDTO space) {
                  if (space.getHomePageType() == HomePages.APP.getValue()) {
                      ShortCutRouter.route(SpMobil.getUser(), space.getHomePageParameter(), "Component", null, null, null);
                  } else {
                      NavigationPage page = new NavigationPage();
                      page.setRootSpaceId(id);
                      page.show();
                  }
              }
          };
          action.attempt();
      }
  }
}
