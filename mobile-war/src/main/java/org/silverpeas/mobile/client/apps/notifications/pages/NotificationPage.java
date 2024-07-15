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

package org.silverpeas.mobile.client.apps.notifications.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiHandler;
import org.silverpeas.mobile.client.apps.notifications.resources.NotificationsMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.event.selection.UsersSelectionSendedEvent;
import org.silverpeas.mobile.client.components.userselection.UserSelectionPage;
import org.silverpeas.mobile.client.components.userselection.events.pages.AllowedUsersAndGroupsLoadedEvent;
import org.silverpeas.mobile.client.components.userselection.widgets.UserGroupItem;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.GroupDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: svu
 */
public class NotificationPage extends UserSelectionPage {

  private NotificationsMessages msgNotification = null;

  private boolean standAlone = true;

  public NotificationPage() {
    super();
    msgNotification = GWT.create(NotificationsMessages.class);
  }

  @Override
  public void onAllowedUsersAndGroupsLoaded(AllowedUsersAndGroupsLoadedEvent allowedUsersAndGroupsLoadedEvent) {
    setTitle();
    for (BaseDTO data : allowedUsersAndGroupsLoadedEvent.getListAllowedUsersAndGroups()) {
      if (allowedUsersAndGroupsLoadedEvent.isUserOnly() && data instanceof GroupDTO) {
        continue;
      }
      UserGroupItem item = new UserGroupItem();
      item.setData(data);
      list.add(item);
    }
  }

  @UiHandler("continu")
  protected void prepareToSend(ClickEvent event) {
    List<BaseDTO> receivers = new ArrayList<BaseDTO>();
    Iterator it = list.iterator();
    while (it.hasNext()) {
      UserGroupItem item = (UserGroupItem) it.next();
      if (item.isSelected()) {
        BaseDTO d = item.getData();
        if (d instanceof UserDTO) {
          ((UserDTO) d).setAvatar("");
        }
        receivers.add(d);
      }
    }

    if (standAlone) {
      NotificationSenderPage page = new NotificationSenderPage();
      page.setPageTitle(msgNotification.notifyContent());
      page.setSelection(receivers);
      page.setTitle(getTitle());
      page.show();
    } else {
      EventBus.getInstance().fireEvent(new UsersSelectionSendedEvent(receivers));
      back();
    }
  }

  @Override
  public void show() {
    super.show();
  }

  @Override
  public void stop() {
    super.stop();
  }

  public void setStandAlone(boolean standAlone) {
    this.standAlone = standAlone;
  }
}