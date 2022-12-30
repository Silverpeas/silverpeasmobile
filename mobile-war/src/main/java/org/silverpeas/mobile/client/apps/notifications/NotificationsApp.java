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

package org.silverpeas.mobile.client.apps.notifications;

import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.notifications.events.app.AbstractNotificationsAppEvent;
import org.silverpeas.mobile.client.apps.notifications.events.app.NotificationsAppEventHandler;
import org.silverpeas.mobile.client.apps.notifications.events.app.SendNotificationEvent;
import org.silverpeas.mobile.client.apps.notifications.events.pages.NotificationSendedEvent;
import org.silverpeas.mobile.client.apps.notifications.pages.NotificationPage;
import org.silverpeas.mobile.client.apps.notifications.resources.NotificationsMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.components.userselection.events.pages.AllowedUsersAndGroupsLoadedEvent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationToSendDTO;

import java.util.List;

/**
 * @author: svu
 */
public class NotificationsApp extends App implements NotificationsAppEventHandler {

  private NotificationPage mainPage = new NotificationPage();
  private String instanceId, contentId, contentType;
  private ApplicationMessages globalMsg = null;
  private NotificationsMessages msg = null;

  private boolean standAlone = true;
  private boolean userOnly = false;

  public NotificationsApp(String instanceId, String contentId, String contentType, String title,
      String pageTitle) {
    super();
    globalMsg = GWT.create(ApplicationMessages.class);
    msg = GWT.create(NotificationsMessages.class);
    this.instanceId = instanceId;
    this.contentId = contentId;
    this.contentType = contentType;
    EventBus.getInstance().addHandler(AbstractNotificationsAppEvent.TYPE, this);
    mainPage.setTitle(title);
    mainPage.setPageTitle(msg.notifyContent());
  }

  public void start() {
    setMainPage(mainPage);
    super.start();
    loadUsersAndGroups();
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractNotificationsAppEvent.TYPE, this);
    super.stop();
  }

  public void loadUsersAndGroups() {

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<BaseDTO>>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceNotifications()
            .getAllowedUsersAndGroups(instanceId, contentId, this);
      }

      @Override
      public void onSuccess(final Method method, final List<BaseDTO> baseDTOS) {
        super.onSuccess(method, baseDTOS);
        // Notify view
        EventBus.getInstance().fireEvent(new AllowedUsersAndGroupsLoadedEvent(baseDTOS, userOnly));
      }
    };
    action.attempt();
  }

  @Override
  public void sendNotification(final SendNotificationEvent event) {

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        super.attempt();
        NotificationDTO n = event.getNotification();
        n.setContentId(contentId);
        n.setContentType(contentType);
        n.setInstanceId(instanceId);

        NotificationToSendDTO dto = new NotificationToSendDTO();
        dto.setNotification(n);
        dto.setReceivers(event.getReceivers());
        dto.setSubject(event.getSubject());
        ServicesLocator.getServiceNotifications().send(dto, this);
      }

      @Override
      public void onSuccess(final Method method, final Void unused) {
        super.onSuccess(method, unused);
        // Notify view
        EventBus.getInstance().fireEvent(new NotificationSendedEvent());
      }
    };
    action.attempt();
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {

  }

  public void setStandAlone(boolean standAlone) {
    this.standAlone = standAlone;
    if (mainPage != null) mainPage.setStandAlone(standAlone);
  }

  public void setUserOnly(boolean userOnly) {
    this.userOnly = userOnly;
  }
}
