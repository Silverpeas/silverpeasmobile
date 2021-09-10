/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.apps.notificationsbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
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
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.notifications.NotificationReceivedDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationSendedDTO;

import java.util.ArrayList;
import java.util.List;

public class NotificationsBoxApp extends App implements NotificationsBoxAppEventHandler, NavigationEventHandler {

    private ApplicationMessages msg;

    public NotificationsBoxApp(){
        super();
        msg = GWT.create(ApplicationMessages.class);
        EventBus.getInstance().addHandler(AbstractNotificationsBoxAppEvent.TYPE, this);
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
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {

  }

  @Override
  public void loadNotifications(final NotificationsLoadEvent notificationsLoadEvent) {
    final String key = "notificationsBox";
    Command offlineAction = new Command() {
      @Override
      public void execute() {
        List<NotificationReceivedDTO> result = LocalStorageHelper.load(key, List.class);
        if (result == null) {
          result = new ArrayList<NotificationReceivedDTO>();
        }
        EventBus.getInstance().fireEvent(new NotificationsLoadedEvent(result));
      }
    };

    AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<List<NotificationReceivedDTO>>(offlineAction) {
      @Override
      public void attempt() {
        ServicesLocator.getServiceNotifications().getUserNotifications(this);
      }

      @Override
      public void onSuccess(List<NotificationReceivedDTO> result) {
        super.onSuccess(result);
        LocalStorageHelper.store(key, List.class, result);
        EventBus.getInstance().fireEvent(new NotificationsLoadedEvent(result));
      }
    };
    action.attempt();
  }

  @Override
  public void readenNotification(final NotificationReadenEvent event) {
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceNotifications().markAsReaden(event.getData().getIdNotif(), this);
      }
    };
    action.attempt();
  }

  @Override
  public void deleteNotifications(final DeleteNotificationsEvent event) {

    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceNotifications().delete(event.getSelection(), this);
      }

      @Override
      public void onSuccess(final Void result) {
        super.onSuccess(result);
        if (event.getSelection().get(0) instanceof NotificationSendedDTO) {
          loadNotificationsSended(new NotificationsSendedLoadEvent());
        } else {
          loadNotifications(new NotificationsLoadEvent());
        }
      }
    };
    action.attempt();
  }

  @Override
  public void markAsReadNotifications(final MarkAsReadNotificationsEvent event) {
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceNotifications().markAsRead(event.getSelection(), this);
      }

      @Override
      public void onSuccess(final Void result) {
        super.onSuccess(result);
        loadNotifications(new NotificationsLoadEvent());
      }
    };
    action.attempt();
  }

  @Override
  public void loadNotificationsSended(
      final NotificationsSendedLoadEvent notificationsSendedLoadEvent) {
    final String key = "notificationsSendedBox";
    Command offlineAction = new Command() {
      @Override
      public void execute() {
        List<NotificationSendedDTO> result = LocalStorageHelper.load(key, List.class);
        if (result == null) {
          result = new ArrayList<NotificationSendedDTO>();
        }
        EventBus.getInstance().fireEvent(new NotificationsSendedLoadedEvent(result));
      }
    };

    AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<List<NotificationSendedDTO>>(offlineAction) {
      @Override
      public void attempt() {
        ServicesLocator.getServiceNotifications().getUserSendedNotifications(this);
      }

      @Override
      public void onSuccess(List<NotificationSendedDTO> result) {
        super.onSuccess(result);
        LocalStorageHelper.store(key, List.class, result);
        EventBus.getInstance().fireEvent(new NotificationsSendedLoadedEvent(result));
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
