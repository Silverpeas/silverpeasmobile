package com.silverpeas.mobile.client.apps.notifications.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.silverpeas.mobile.client.apps.contacts.events.app.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.notifications.events.app.SendNotificationEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.AbstractNotificationPagesEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.AllowedUsersAndGroupsLoadedEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.NotificationPagesEventHandler;
import com.silverpeas.mobile.client.apps.notifications.events.pages.NotificationSendedEvent;
import com.silverpeas.mobile.client.apps.notifications.pages.widgets.UserGroupItem;
import com.silverpeas.mobile.client.apps.notifications.pages.widgets.events.AbstractSelectionEvent;
import com.silverpeas.mobile.client.apps.notifications.pages.widgets.events.ChangeEvent;
import com.silverpeas.mobile.client.apps.notifications.pages.widgets.events.SelectionEventHandler;
import com.silverpeas.mobile.client.apps.notifications.resources.NotificationsMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.GroupDTO;
import com.silverpeas.mobile.shared.dto.UserDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: svu
 */
public class NotificationPage extends PageContent implements View, NotificationPagesEventHandler, SelectionEventHandler {

  private static NotificationPageUiBinder uiBinder = GWT.create(NotificationPageUiBinder.class);

  interface NotificationPageUiBinder extends UiBinder<HTMLPanel, NotificationPage> {
  }

  @UiField(provided = true) protected NotificationsMessages msg = null;
  @UiField protected HTMLPanel container;
  @UiField protected Anchor continu;
  @UiField UnorderedList list;
  @UiField TextBox filter;

  private int nbUserSelected = 0;

  public NotificationPage() {
    msg = GWT.create(NotificationsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("notification");

    EventBus.getInstance().addHandler(AbstractNotificationPagesEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractSelectionEvent.TYPE, this);
    continu.setVisible(false);
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
    page.setPageTitle(msg.notifyContent());
    page.setSelection(receivers);
    page.setTitle(getTitle());
    page.show();

  }

  @UiHandler("filter")
  protected void filter(KeyUpEvent event) {

    String fi = filter.getText();
    fi = fi.toLowerCase();
    Iterator it = list.iterator();
    while (it.hasNext()) {
      UserGroupItem item = (UserGroupItem) it.next();
      if (!item.isSelected()) { // keep selected items visibles
        BaseDTO d = item.getData();
        if (fi.isEmpty()) {
          item.setVisible(true);
        } else {
          String value = "";
          if (d instanceof UserDTO) {
            value = ((UserDTO) d).getLastName();
          } else if (d instanceof GroupDTO) {
            value = ((GroupDTO) d).getName();
          }
          value = value.toLowerCase();
          if (value.startsWith(filter.getText())) {
            item.setVisible(true);
          } else {
            item.setVisible(false);
          }
        }
      }
    }
  }

  @Override
  public void show() {
    super.show();
    nbUserSelected = 0;
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractNotificationPagesEvent.TYPE, this);
    EventBus.getInstance().removeHandler(AbstractSelectionEvent.TYPE, this);
    nbUserSelected = 0;
  }

  @Override
  public void onNotificationSended(NotificationSendedEvent event) {
  }

  @Override
  public void onSelectionChange(ChangeEvent event) {
    if (event.isSelect()) {
      nbUserSelected++;
    } else {
      nbUserSelected--;
    }
    continu.setVisible((nbUserSelected > 0));

    setTitle();

  }

  private void setTitle() {
    Element title = DOM.getElementById("title-instruction");
    title.setInnerText(msg.title(nbUserSelected));
  }
}