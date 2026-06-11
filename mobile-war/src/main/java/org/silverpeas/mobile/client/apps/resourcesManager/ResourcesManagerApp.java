/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.client.apps.resourcesManager;

import com.google.gwt.core.client.GWT;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.app.*;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.DeletedReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.SavedReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.pages.ReservationSelectionPage;
import org.silverpeas.mobile.client.apps.resourcesManager.pages.ResourcesManagerPage;
import org.silverpeas.mobile.client.apps.resourcesManager.resources.ResourcesManagerMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.rest.RestMethod;
import org.silverpeas.mobile.client.common.network.rest.RestMethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.components.Snackbar;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.reservations.Errors;
import org.silverpeas.mobile.shared.dto.reservations.ReservationDTO;
import org.silverpeas.mobile.shared.dto.reservations.ResourceDTO;

import java.util.List;

public class ResourcesManagerApp extends App
    implements ResourcesManagerAppEventHandler, NavigationEventHandler {

  private ResourcesManagerMessages msg;

  public ResourcesManagerApp() {
    super();
    msg = GWT.create(ResourcesManagerMessages.class);
    EventBus.getInstance().addHandler(AbstractResourcesManagerAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  public void start() {
    // always start
  }

  @Override
  public void stop() {
    // nevers stop
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
    if (event.getInstance().getType().equals(Apps.resourcesManager.name())) {
      setApplicationInstance(event.getInstance());

      RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<List<ReservationDTO>>() {
        @Override
        public void attempt() {
          super.attempt();
          ServicesLocator.getServiceResourcesManager().getMyReservations(getApplicationInstance().getId(), this);
        }

        @Override
        public void onSuccess(final RestMethod method, final List<ReservationDTO> reservationDTOS) {
          super.onSuccess(method, reservationDTOS);
          ResourcesManagerPage page = new ResourcesManagerPage();
          page.setApp(ResourcesManagerApp.this);
          page.setPageTitle(event.getInstance().getLabel());
          page.setData(reservationDTOS);
          setMainPage(page);
          page.show();
        }
      };
      action.attempt();
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getInstanceId() == null || !event.getContent().getInstanceId().startsWith(Apps.resourcesManager.name())) return;
    if (event.getContent().getType().equals("Component")) {
      super.showContent(event);
    }
  }

  @Override
  public void addReservation(final AddReservationEvent event) {
    RestMethodCallbackOnlineOnly checkAction = new RestMethodCallbackOnlineOnly<String>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceResourcesManager()
            .checkDates(getApplicationInstance().getId(), event.getData().getStartDate(), event.getData().getEndDate(),this);
      }

      @Override
      public void onSuccess(final RestMethod method, final String result) {
        super.onSuccess(method, result);
        if (result == null) {
          RestMethodCallbackOnlineOnly action =
              new RestMethodCallbackOnlineOnly<List<ResourceDTO>>() {
                @Override
                public void attempt() {
                  super.attempt();
                  ServicesLocator.getServiceResourcesManager()
                      .getAvailableResources(getApplicationInstance().getId(),
                          event.getData().getStartDate(), event.getData().getEndDate(),
                          this);
                }

                @Override
                public void onSuccess(final RestMethod method,
                    final List<ResourceDTO> resources) {
                  super.onSuccess(method, resources);

                  ReservationSelectionPage page = new ReservationSelectionPage();
                  page.setPageTitle(msg.resourcesSelection());
                  page.setReservation(event.getData());
                  page.setResources(resources);
                  page.show();
                }
              };
          action.attempt();
        } else if (result.equals(Errors.dateOrder.toString())) {
          Snackbar.showWithCloseButton(msg.errorDateOrder(), Snackbar.ERROR);
        } else if (result.equals(Errors.earlierDate.toString())) {
          Snackbar.showWithCloseButton(msg.errorEarlierDate(), Snackbar.ERROR);
        }
      }
    };
    checkAction.attempt();
  }

  @Override
  public void saveReservation(final SaveReservationEvent saveReservationEvent) {
    RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<ReservationDTO>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceResourcesManager()
            .saveReservation(getApplicationInstance().getId(), saveReservationEvent.getData(), this);
      }

      @Override
      public void onSuccess(final RestMethod method, final ReservationDTO reservation) {
        super.onSuccess(method, reservation);
        Snackbar.show(msg.saved(), Snackbar.DELAY, Snackbar.INFO);
        getMainPage().back(2);
        SavedReservationEvent event = new SavedReservationEvent();
        event.setData(reservation);
        EventBus.getInstance().fireEvent(event);
      }
    };
    action.attempt();
  }

  @Override
  public void deleteReservation(final DeleteReservationEvent deleteReservationEvent) {
    RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceResourcesManager()
            .deleteReservation(getApplicationInstance().getId(), deleteReservationEvent.getData(), this);
      }

      @Override
      public void onSuccess(final RestMethod method, final Void unused) {
        super.onSuccess(method, unused);
        getMainPage().back();
        DeletedReservationEvent event = new DeletedReservationEvent();
        event.setData(deleteReservationEvent.getData());
        EventBus.getInstance().fireEvent(event);
      }
    };
    action.attempt();
  }
}
