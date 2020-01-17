/*
 * Copyright (C) 2000 - 2019 Silverpeas
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

package org.silverpeas.mobile.client.apps.notificationsbox.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationsLoadEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.pages.AbstractNotificationsBoxPagesEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.pages.NotificationsBoxPagesEventHandler;
import org.silverpeas.mobile.client.apps.notificationsbox.events.pages.NotificationsLoadedEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.pages.widgets.DeleteButton;
import org.silverpeas.mobile.client.apps.notificationsbox.pages.widgets.NotificationItem;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.notifications.NotificationReceivedDTO;

import java.util.ArrayList;
import java.util.List;

public class NotificationsBoxPage extends PageContent implements NotificationsBoxPagesEventHandler {

  private static NotificationsBoxPageUiBinder uiBinder = GWT.create(NotificationsBoxPageUiBinder.class);

  @UiField(provided = true) protected ApplicationMessages msg = null;
  @UiField
  UnorderedList notifications;

  @UiField
  ActionsMenu actionsMenu;

  private DeleteButton delete = new DeleteButton();

  interface NotificationsBoxPageUiBinder extends UiBinder<Widget, NotificationsBoxPage> {
  }

  public NotificationsBoxPage() {
    msg = GWT.create(ApplicationMessages.class);
    setPageTitle(msg.notifications());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractNotificationsBoxPagesEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new NotificationsLoadEvent());
    delete.setParentPage(this);
    actionsMenu.addAction(delete);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractNotificationsBoxPagesEvent.TYPE, this);
  }

  @Override
  public void onNotificationsLoaded(final NotificationsLoadedEvent event) {
    notifications.clear();
    List<NotificationReceivedDTO> notifs = event.getNotifications();
    for (NotificationReceivedDTO notif : notifs) {
      NotificationItem item = new NotificationItem();
      item.setData(notif);
      notifications.add(item);
    }
  }

  public List<NotificationReceivedDTO> getSelectedNotification() {
    List<NotificationReceivedDTO> selection = new ArrayList<>();
    for (int i = 0; i < notifications.getCount(); i++) {
      NotificationItem item = (NotificationItem) notifications.getWidget(i);
      if (item.isSelected()) {
        selection.add(item.getData());
      }
    }
    return selection;
  }
}