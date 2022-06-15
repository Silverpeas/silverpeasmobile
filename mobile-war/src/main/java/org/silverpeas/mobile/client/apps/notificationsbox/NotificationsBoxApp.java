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

package org.silverpeas.mobile.client.apps.notificationsbox;

import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.AbstractNotificationsBoxAppEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.DeleteNotificationsEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.MarkAsReadNotificationsEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationReadenEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationsBoxAppEventHandler;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationsLoadEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationsSendedLoadEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.pages.NotificationsLoadedEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.pages.NotificationsSendedLoadedEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.pages.NotificationsBoxPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineBackground;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.notifications.NotificationReceivedDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationSendedDTO;

import java.util.List;

public class NotificationsBoxApp extends App
    implements NotificationsBoxAppEventHandler, NavigationEventHandler {

  private ApplicationMessages msg;

  public NotificationsBoxApp() {
    super();
    msg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(AbstractNotificationsBoxAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  public void start() {
    // no "super.start(lauchingPage);" this apps is used in another apps
  }

  @Override
  public void stop() {
    // never stop
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {

  }

  @Override
  public void loadNotifications(final NotificationsLoadEvent event) {
    loadNotifications(event,0);
  }

  private void loadNotifications(NotificationsLoadEvent event, int nbCall) {
    MethodCallbackOnlineBackground action =
        new MethodCallbackOnlineBackground<StreamingList<NotificationReceivedDTO>>() {
          @Override
          public void attempt() {
            super.attempt();
            ServicesLocator.getServiceNotifications().getUserNotifications(nbCall+1, this);
          }

          @Override
          public void onSuccess(final Method method,
              final StreamingList<NotificationReceivedDTO> notificationReceivedDTOS) {
            super.onSuccess(method, notificationReceivedDTOS);
            EventBus.getInstance()
                .fireEvent(new NotificationsLoadedEvent(notificationReceivedDTOS));
            if (notificationReceivedDTOS.getMoreElement()) {
              loadNotifications(event, nbCall+1);
            }
          }
        };
    action.attempt();
  }

  @Override
  public void readenNotification(final NotificationReadenEvent event) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceNotifications().markAsReaden(event.getData().getIdNotif(), this);
      }
    };
    action.attempt();
  }

  @Override
  public void deleteNotifications(final DeleteNotificationsEvent event) {

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceNotifications().delete(event.getSelection(), this);
      }

      @Override
      public void onSuccess(final Method method, final Void unused) {
        super.onSuccess(method, unused);
        if (event.getSelection().get(0) instanceof NotificationSendedDTO) {
          loadNotificationsSended(new NotificationsSendedLoadEvent());
        } else {
          loadNotifications(new NotificationsLoadEvent(), 0);
        }
      }
    };
    action.attempt();
  }

  @Override
  public void markAsReadNotifications(final MarkAsReadNotificationsEvent event) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceNotifications().markAsRead(event.getSelection(), this);
      }

      @Override
      public void onSuccess(final Method method, final Void unused) {
        super.onSuccess(method, unused);
        loadNotifications(new NotificationsLoadEvent(), 0);
      }
    };
    action.attempt();
  }

  @Override
  public void loadNotificationsSended(
      final NotificationsSendedLoadEvent notificationsSendedLoadEvent) {
    loadNotificationsSended(notificationsSendedLoadEvent, 0);
  }

  private void loadNotificationsSended(NotificationsSendedLoadEvent event, int nbCall) {
    MethodCallbackOnlineOnly action =
        new MethodCallbackOnlineOnly<StreamingList<NotificationSendedDTO>>() {
          @Override
          public void attempt() {
            super.attempt();
            ServicesLocator.getServiceNotifications().getUserSendedNotifications(nbCall+1, this);
          }

          @Override
          public void onSuccess(final Method method,
              final StreamingList<NotificationSendedDTO> notificationSendedDTOS) {
            super.onSuccess(method, notificationSendedDTOS);
            EventBus.getInstance()
                .fireEvent(new NotificationsSendedLoadedEvent(notificationSendedDTOS));
          }
        };
    action.attempt();
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals(ContentsTypes.NotificationsBox.toString())) {
      NotificationsBoxPage page = new NotificationsBoxPage();
      setMainPage(page);
      page.show();
    }
  }
}
