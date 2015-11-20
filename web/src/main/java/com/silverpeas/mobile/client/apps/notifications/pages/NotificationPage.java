package com.silverpeas.mobile.client.apps.notifications.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.silverpeas.mobile.client.apps.notifications.events.app.SendNotificationEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.AbstractNotificationPagesEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.AllowedUsersAndGroupsLoadedEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.NotificationPagesEventHandler;
import com.silverpeas.mobile.client.apps.notifications.events.pages.NotificationSendedEvent;
import com.silverpeas.mobile.client.apps.notifications.pages.widgets.UserGroupItem;
import com.silverpeas.mobile.client.apps.notifications.resources.NotificationsMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.UserDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: svu
 */
public class NotificationPage extends PageContent implements View, NotificationPagesEventHandler {

  private static NotificationPageUiBinder uiBinder = GWT.create(NotificationPageUiBinder.class);

  interface NotificationPageUiBinder extends UiBinder<HTMLPanel, NotificationPage> {
  }

  @UiField(provided = true) protected NotificationsMessages msg = null;
  @UiField protected HTMLPanel container;
  @UiField protected Anchor send;
  @UiField UnorderedList list;
  @UiField TextArea message;


  public NotificationPage() {
    msg = GWT.create(NotificationsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("update-statut");
    EventBus.getInstance().addHandler(AbstractNotificationPagesEvent.TYPE, this);
  }

  @Override
  public void onAllowedUsersAndGroupsLoaded(AllowedUsersAndGroupsLoadedEvent allowedUsersAndGroupsLoadedEvent) {
    for (BaseDTO data : allowedUsersAndGroupsLoadedEvent.getListAllowedUsersAndGroups()) {
      UserGroupItem item = new UserGroupItem();
      item.setData(data);
      list.add(item);
    }
  }

  @UiHandler("send")
  protected void sendNotification(ClickEvent event) {
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
    if (receivers.isEmpty()) return;
    NotificationDTO notification = new NotificationDTO(message.getText());
    EventBus.getInstance().fireEvent(new SendNotificationEvent(notification, receivers));

  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractNotificationPagesEvent.TYPE, this);
  }

  @Override
  public void onNotificationSended(NotificationSendedEvent event) {
    back();
  }
}