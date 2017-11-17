/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package org.silverpeas.mobile.client.components.userselection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.userselection.events.components
    .UsersAndGroupsSelectedEvent;
import org.silverpeas.mobile.client.components.userselection.events.pages
    .AbstractUserSelectionPagesEvent;
import org.silverpeas.mobile.client.components.userselection.events.pages
    .AllowedUsersAndGroupsLoadedEvent;
import org.silverpeas.mobile.client.components.userselection.events.pages
    .UserSelectionPagesEventHandler;
import org.silverpeas.mobile.client.components.userselection.resources.UserSelectionMessages;
import org.silverpeas.mobile.client.components.userselection.widgets.UserGroupItem;
import org.silverpeas.mobile.client.components.userselection.widgets.events.AbstractSelectionEvent;
import org.silverpeas.mobile.client.components.userselection.widgets.events.ChangeEvent;
import org.silverpeas.mobile.client.components.userselection.widgets.events.SelectionEventHandler;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.GroupDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: svu
 */
public class UserSelectionPage extends PageContent
    implements View, UserSelectionPagesEventHandler, SelectionEventHandler {

  private static UserSelectionPageUiBinder uiBinder = GWT.create(UserSelectionPageUiBinder.class);
  private String contentId;
  private List<String> preSelectedIds = new ArrayList<String>();

  public void setMaxSelection(final int maxSelection) {
    this.maxSelection = maxSelection;
  }

  public void setPreSelectedIds(final List<String> preSelectedIds) {
    this.preSelectedIds = preSelectedIds;
  }

  interface UserSelectionPageUiBinder extends UiBinder<HTMLPanel, UserSelectionPage> {
  }

  @UiField(provided = true) protected UserSelectionMessages msg = null;
  @UiField protected HTMLPanel container;
  @UiField protected Anchor continu;
  @UiField protected UnorderedList list;
  @UiField protected TextBox filter;

  private int nbUserSelected = 0;
  private int maxSelection = 0;

  public UserSelectionPage() {
    msg = GWT.create(UserSelectionMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("notification");
    filter.getElement().setAttribute("placeholder", msg.filter());

    EventBus.getInstance().addHandler(AbstractUserSelectionPagesEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractSelectionEvent.TYPE, this);
    continu.setVisible(false);
  }

  public void setContentId(final String contentId) {
    this.contentId = contentId;
  }

  @Override
  public void onAllowedUsersAndGroupsLoaded(AllowedUsersAndGroupsLoadedEvent allowedUsersAndGroupsLoadedEvent) {
    setTitle();
    for (BaseDTO data : allowedUsersAndGroupsLoadedEvent.getListAllowedUsersAndGroups()) {
      UserGroupItem item = new UserGroupItem();
      item.setData(data);
      if (preSelectedIds.contains(data.getId())) {
        item.select();
      }
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
    UsersAndGroupsSelectedEvent ev = new UsersAndGroupsSelectedEvent(receivers);
    ev.setContentId(contentId);
    EventBus.getInstance().fireEvent(ev);
    back();
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
          if (value.startsWith(filter.getText().toLowerCase())) {
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
    EventBus.getInstance().removeHandler(AbstractUserSelectionPagesEvent.TYPE, this);
    EventBus.getInstance().removeHandler(AbstractSelectionEvent.TYPE, this);
    nbUserSelected = 0;
  }

  @Override
  public void onSelectionChange(ChangeEvent event) {
    if (maxSelection == 1) {
      for (int i = 0; i < list.getWidgetCount(); i++) {
        UserGroupItem item = (UserGroupItem)list.getWidget(i);
        if (item != event.getItem()) {
          item.unSelect();
        }
        nbUserSelected = 0;
        if (event.getItem().isSelected()) {
          nbUserSelected = 1;
        }
      }
    } else {
      if (event.getItem().isSelected()) {
        nbUserSelected++;
      } else {
        nbUserSelected--;
      }
    }
    continu.setVisible((nbUserSelected > 0));

    setTitle();

  }

  protected void setTitle() {
    Element title = DOM.getElementById("title-instruction");
    title.setInnerText(msg.title(nbUserSelected));
  }
}