/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.client.apps.favorites;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.apps.favorites.events.app.AbstractFavoritesAppEvent;
import org.silverpeas.mobile.client.apps.favorites.events.app.AddFavoriteEvent;
import org.silverpeas.mobile.client.apps.favorites.events.app.FavoritesAppEventHandler;
import org.silverpeas.mobile.client.apps.favorites.events.app.FavoritesLoadEvent;
import org.silverpeas.mobile.client.apps.favorites.events.app.GotoAppEvent;
import org.silverpeas.mobile.client.apps.favorites.events.pages.FavoritesLoadedEvent;
import org.silverpeas.mobile.client.apps.favorites.pages.FavoritesPage;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.FavoriteDTO;
import org.silverpeas.mobile.shared.dto.MyLinkDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

import java.util.ArrayList;
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
          List<MyLinkDTO> result = LocalStorageHelper.load(key, List.class);
          if (result == null) {
            result = new ArrayList<MyLinkDTO>();
          }
          EventBus.getInstance().fireEvent(new FavoritesLoadedEvent(result));
        }
      };


      MethodCallbackOnlineOrOffline action = new MethodCallbackOnlineOrOffline<List<MyLinkDTO>>(offlineAction) {
        @Override
        public void onSuccess(final Method method, final List<MyLinkDTO> result) {
          super.onSuccess(method, result);
          LocalStorageHelper.store(key, List.class, result);
          EventBus.getInstance().fireEvent(new FavoritesLoadedEvent(result));
        }

        @Override
        public void attempt() {
          ServicesLocator.getServiceMyLinks().getMyLinks(this);
        }
      };
      action.attempt();
    }

  @Override
  public void addFavorite(final AddFavoriteEvent event) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<MyLinkDTO>() {
      @Override
      public void onSuccess(final Method method, final MyLinkDTO myLinkDTO) {
        super.onSuccess(method, myLinkDTO);
        //TODO : message ?
      }

      @Override
      public void attempt() {
        MyLinkDTO dto = new MyLinkDTO();
        dto.setName(event.getDescription());
        dto.setUrl("/" + event.getObjectType() + "/" + event.getObjectId());
        dto.setVisible(true);
        dto.setPopup(false);
        dto.setDescription(event.getDescription());
        ServicesLocator.getServiceMyLinks().addLink(dto, this);
      }

    };
    action.attempt();
  }

  @Override
  public void gotoApp(final GotoAppEvent event) {
    ServicesLocator.getServiceNavigation().getApp(event.getInstanceId(), null, null,
        new AsyncCallback<ApplicationInstanceDTO>() {
          @Override
          public void onFailure(final Throwable caught) {
            Notification.activityStop();
            EventBus.getInstance().fireEvent(new ErrorEvent(caught));
          }

          @Override
          public void onSuccess(final ApplicationInstanceDTO applicationInstanceDTO) {
            Notification.activityStop();
            EventBus.getInstance().fireEvent(new NavigationAppInstanceChangedEvent(applicationInstanceDTO));
          }
        });
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
