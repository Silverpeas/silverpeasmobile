package com.silverpeas.mobile.client.apps.notifications.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiHandler;
import com.silverpeas.mobile.client.apps.notifications.resources.NotificationsMessages;
import com.silverpeas.mobile.client.components.userselection.UserSelectionPage;
import com.silverpeas.mobile.client.components.userselection.events.pages.AllowedUsersAndGroupsLoadedEvent;
import com.silverpeas.mobile.client.components.userselection.widgets.UserGroupItem;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.UserDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: svu
 */
public class NotificationPage extends UserSelectionPage {

  private NotificationsMessages msgNotification = null;

  public NotificationPage() {
    super();
    msgNotification = GWT.create(NotificationsMessages.class);
  }

  @Override
  public void onAllowedUsersAndGroupsLoaded(AllowedUsersAndGroupsLoadedEvent allowedUsersAndGroupsLoadedEvent) {
    setTitle();
    for (BaseDTO data : allowedUsersAndGroupsLoadedEvent.getListAllowedUsersAndGroups()) {
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

    NotificationSenderPage page = new NotificationSenderPage();
    page.setPageTitle(msgNotification.notifyContent());
    page.setSelection(receivers);
    page.setTitle(getTitle());
    page.show();

  }

  @Override
  public void show() {
    super.show();
  }

  @Override
  public void stop() {
    super.stop();
  }
}