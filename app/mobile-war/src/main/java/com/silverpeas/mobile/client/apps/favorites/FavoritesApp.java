/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package com.silverpeas.mobile.client.apps.favorites;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.silverpeas.mobile.client.apps.favorites.events.app.AbstractFavoritesAppEvent;
import com.silverpeas.mobile.client.apps.favorites.events.app.AddFavoriteEvent;
import com.silverpeas.mobile.client.apps.favorites.events.app.FavoritesAppEventHandler;
import com.silverpeas.mobile.client.apps.favorites.events.app.FavoritesLoadEvent;
import com.silverpeas.mobile.client.apps.favorites.events.pages.FavoritesLoadedEvent;
import com.silverpeas.mobile.client.apps.favorites.pages.FavoritesPage;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external
    .NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import com.silverpeas.mobile.client.apps.status.StatusApp;
import com.silverpeas.mobile.client.apps.status.events.StatusEvents;
import com.silverpeas.mobile.client.apps.tasks.pages.TasksPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.client.components.base.events.page.PageEvent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.FavoriteDTO;
import com.silverpeas.mobile.shared.dto.StatusDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavoritesApp extends App implements FavoritesAppEventHandler, NavigationEventHandler {

    private ApplicationMessages msg;

    public FavoritesApp(){
        super();
        msg = GWT.create(ApplicationMessages.class);
        EventBus.getInstance().addHandler(AbstractFavoritesAppEvent.TYPE, this);
        EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
    }

    public void start(){
      // no "super.start(lauchingPage);" this apps is used in another apps
    }

    @Override
    public void stop() {
      // never stop
    }

    @Override
    public void loadFavorites(final FavoritesLoadEvent event) {
      final String key = "favorites";
      Command offlineAction = new Command() {

        @Override
        public void execute() {
          List<FavoriteDTO> result = LocalStorageHelper.load(key, List.class);
          if (result == null) {
            result = new ArrayList<FavoriteDTO>();
          }
          EventBus.getInstance().fireEvent(new FavoritesLoadedEvent(result));
        }
      };

      AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<List<FavoriteDTO>>(offlineAction) {
        @Override
        public void attempt() {
          ServicesLocator.getServiceFavorites().getFavorites(this);
        }

        @Override
        public void onSuccess(List<FavoriteDTO> result) {
          super.onSuccess(result);
          LocalStorageHelper.store(key, List.class, result);
          EventBus.getInstance().fireEvent(new FavoritesLoadedEvent(result));
        }
      };
      action.attempt();
    }

  @Override
  public void addFavorite(final AddFavoriteEvent event) {
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceFavorites().addFavorite(event.getInstanceId(), event.getObjectId(), event.getObjectType(), event.getDescription(), this);
      }

      public void onSuccess(String result) {
        //TODO : message ?
      }
    };
    action.attempt();
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) { /* only one instance */ }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals(ContentsTypes.Favortis.toString())) {
      FavoritesPage page = new FavoritesPage();
      setMainPage(page);
      page.show();
    }
  }
}
