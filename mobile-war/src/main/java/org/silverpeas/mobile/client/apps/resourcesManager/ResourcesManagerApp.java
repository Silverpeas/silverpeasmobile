/*
 * Copyright (C) 2000 - 2021 Silverpeas
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
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.app.AbstractResourcesManagerAppEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.app.AddReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.app.DeleteReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.app.ResourcesManagerAppEventHandler;
import org.silverpeas.mobile.client.apps.resourcesManager.events.app.SaveReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.DeletedReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.SavedReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.pages.ReservationSelectionPage;
import org.silverpeas.mobile.client.apps.resourcesManager.pages.ResourcesManagerPage;
import org.silverpeas.mobile.client.apps.resourcesManager.resources.ResourcesManagerMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.TextCallbackOnlineOnly;
import org.silverpeas.mobile.client.components.Popin;
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

      MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<ReservationDTO>>() {
        @Override
        public void attempt() {
          super.attempt();
          ServicesLocator.getServiceResourcesManager().getMyReservations(getApplicationInstance().getId(), this);
        }

        @Override
        public void onSuccess(final Method method, final List<ReservationDTO> reservationDTOS) {
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
    if (event.getContent().getType().equals("Component") &&
        event.getContent().getInstanceId().startsWith(Apps.resourcesManager.name())) {
      super.showContent(event);
    }
  }

  @Override
  public void addReservation(final AddReservationEvent event) {
    TextCallbackOnlineOnly checkAction = new TextCallbackOnlineOnly() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceResourcesManager()
            .checkDates(getApplicationInstance().getId(), event.getData().getStartDate(), event.getData().getEndDate(),this);
      }

      @Override
      public void onSuccess(final Method method, final String error) {
        super.onSuccess(method, error);
        if (error.isEmpty()) {
          MethodCallbackOnlineOnly action =
              new MethodCallbackOnlineOnly<List<ResourceDTO>>() {
                @Override
                public void attempt() {
                  super.attempt();
                  ServicesLocator.getServiceResourcesManager()
                      .getAvailableResources(getApplicationInstance().getId(),
                          event.getData().getStartDate(), event.getData().getEndDate(),
                          this);
                }

                @Override
                public void onSuccess(final Method method,
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
        } else if (error.equals(Errors.dateOrder.toString())) {
          new Popin(msg.errorDateOrder()).show();
        } else if (error.equals(Errors.earlierDate.toString())) {
          new Popin(msg.errorEarlierDate()).show();
        }
      }
    };
    checkAction.attempt();
  }

  @Override
  public void saveReservation(final SaveReservationEvent saveReservationEvent) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<ReservationDTO>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceResourcesManager()
            .saveReservation(getApplicationInstance().getId(), saveReservationEvent.getData(), this);
      }

      @Override
      public void onSuccess(final Method method, final ReservationDTO reservation) {
        super.onSuccess(method, reservation);
        new Popin(msg.saved()).show();
        getMainPage().back();
        getMainPage().back();
        SavedReservationEvent event = new SavedReservationEvent();
        event.setData(reservation);
        EventBus.getInstance().fireEvent(event);
      }
    };
    action.attempt();
  }

  @Override
  public void deleteReservation(final DeleteReservationEvent deleteReservationEvent) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceResourcesManager()
            .deleteReservation(getApplicationInstance().getId(), deleteReservationEvent.getData(), this);
      }

      @Override
      public void onSuccess(final Method method, final Void unused) {
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
