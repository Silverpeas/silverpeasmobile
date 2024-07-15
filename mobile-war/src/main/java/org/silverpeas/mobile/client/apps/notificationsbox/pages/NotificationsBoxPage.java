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

package org.silverpeas.mobile.client.apps.notificationsbox.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.DeleteNotificationsEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.MarkAsReadNotificationsEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationsLoadEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationsSendedLoadEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.pages.AbstractNotificationsBoxPagesEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.pages.NotificationsBoxPagesEventHandler;
import org.silverpeas.mobile.client.apps.notificationsbox.events.pages.NotificationsLoadedEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.pages.NotificationsSendedLoadedEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.pages.widgets.MarkAsReadButton;
import org.silverpeas.mobile.client.apps.notificationsbox.pages.widgets.NotificationItem;
import org.silverpeas.mobile.client.apps.notificationsbox.resources.NotificationsMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.PopinConfirmation;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.base.widgets.DeleteButton;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.notifications.NotificationBoxDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationReceivedDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationSendedDTO;

import java.util.ArrayList;
import java.util.List;

public class NotificationsBoxPage extends PageContent implements NotificationsBoxPagesEventHandler {

  private static NotificationsBoxPageUiBinder uiBinder = GWT.create(NotificationsBoxPageUiBinder.class);

  @UiField(provided = true) protected ApplicationMessages msg = null;
  @UiField
  UnorderedList notifications;

  @UiField
  Anchor notificationReceived, notificationSended;

  private NotificationsMessages msgApp = GWT.create(NotificationsMessages .class);
  private DeleteButton buttonDelete = new DeleteButton();
  private MarkAsReadButton buttonNotRead = new MarkAsReadButton();

  interface NotificationsBoxPageUiBinder extends UiBinder<Widget, NotificationsBoxPage> {
  }

  public NotificationsBoxPage() {
    msg = GWT.create(ApplicationMessages.class);
    setPageTitle(msg.notifications());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractNotificationsBoxPagesEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new NotificationsLoadEvent());
    buttonNotRead.setParentPage(this);
    buttonDelete.setId("delete-notifications");
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractNotificationsBoxPagesEvent.TYPE, this);
  }

  @Override
  public void onNotificationsLoaded(final NotificationsLoadedEvent event) {
    if (event.getNotifications().isFirstCall()) {
      notifications.clear();
    }
    List<NotificationReceivedDTO> notifs = event.getNotifications().getList();
    for (NotificationReceivedDTO notif : notifs) {
      NotificationItem item = new NotificationItem();
      item.setData(notif);
      item.setParent(this);
      notifications.add(item);
    }
  }

  @Override
  public void onNotificationsSendedLoaded(
      final NotificationsSendedLoadedEvent event) {
    if (event.getNotifications().isFirstCall()) {
      notifications.clear();
    }
    List<NotificationSendedDTO> notifs = event.getNotifications().getList();
    for (NotificationSendedDTO notif : notifs) {
      NotificationItem item = new NotificationItem();
      item.setData(notif);
      item.setParent(this);
      notifications.add(item);
    }
  }

  public List<NotificationBoxDTO> getSelectedNotification() {
    List<NotificationBoxDTO> selection = new ArrayList<>();
    for (int i = 0; i < notifications.getCount(); i++) {
      NotificationItem item = (NotificationItem) notifications.getWidget(i);
      if (item.isSelected()) {
        selection.add(item.getData());
      }
    }
    return selection;
  }

  @UiHandler("notificationSended")
  protected void showSendedNotifications(ClickEvent event) {
    setSelectionMode(false);
    notificationReceived.removeStyleName("ui-btn-active");
    notificationSended.addStyleName("ui-btn-active");
    buttonNotRead.setVisible(false);
    EventBus.getInstance().fireEvent(new NotificationsSendedLoadEvent());
  }

  @UiHandler("notificationReceived")
  protected void showReceivedNotifications(ClickEvent event) {
    setSelectionMode(false);
    notificationSended.removeStyleName("ui-btn-active");
    notificationReceived.addStyleName("ui-btn-active");
    buttonNotRead.setVisible(true);
    EventBus.getInstance().fireEvent(new NotificationsLoadEvent());
  }

  @Override
  public void setSelectionMode(boolean selectionMode) {
    super.setSelectionMode(selectionMode);
    if (selectionMode) {
      clearActions();
      buttonDelete.setCallback(new Command() {@Override public void execute() {deleteSelectedNotifications();}});
      buttonNotRead.setCallback(new Command() {@Override public void execute() {markAsReadNotifications();}});
      addActionShortcut(buttonNotRead);
      addActionShortcut(buttonDelete);

    } else {
      clearActions();
    }
  }

  private void markAsReadNotifications() {
    List<NotificationBoxDTO> selection = getSelectedNotification();
    MarkAsReadNotificationsEvent notReadEvent = new MarkAsReadNotificationsEvent();
    notReadEvent.setSelection(selection);
    EventBus.getInstance().fireEvent(notReadEvent);
    clearActions();
  }

  private void deleteSelectedNotifications() {
    PopinConfirmation popin = new PopinConfirmation(msgApp.deleteConfirmation());
    popin.setYesCallback(new Command() {
      @Override
      public void execute() {
        List<NotificationBoxDTO> selection = getSelectedNotification();
        DeleteNotificationsEvent deleteEvent = new DeleteNotificationsEvent();
        deleteEvent.setSelection(selection);
        if (!selection.isEmpty()) EventBus.getInstance().fireEvent(deleteEvent);
        clearActions();
      }
    });
    popin.show();
  }
}