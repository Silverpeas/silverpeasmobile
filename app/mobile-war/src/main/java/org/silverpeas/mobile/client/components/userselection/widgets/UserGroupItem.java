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

package org.silverpeas.mobile.client.components.userselection.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.HTMLPanelClickable;
import org.silverpeas.mobile.client.components.base.ActionItem;
import org.silverpeas.mobile.client.components.userselection.widgets.events.ChangeEvent;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.GroupDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;

public class UserGroupItem extends ActionItem {

  private static ContactItemUiBinder uiBinder = GWT.create(ContactItemUiBinder.class);
  @UiField HTML mail;
  @UiField HTMLPanel content;
  @UiField
  HTMLPanelClickable container;

  private BaseDTO data = null;
  private boolean selected = false;

  private ApplicationResources resources = GWT.create(ApplicationResources.class);

  interface ContactItemUiBinder extends UiBinder<Widget, UserGroupItem> {
  }

  public UserGroupItem() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void unSelect() {
    if (isSelected()) toggleSelection();
  }
  public void select() {
    if (!isSelected()) toggleSelection();
  }

  public void setData(BaseDTO data) {
    this.data = data;
    if (data instanceof UserDTO) {
      UserDTO dataUser = (UserDTO) data;
      if (dataUser.getAvatar() == null || dataUser.getAvatar().isEmpty()) {
        Image avatar = new Image(resources.avatar());
        avatar.getElement().removeAttribute("height");
        avatar.getElement().removeAttribute("width");
        content.add(avatar);
      } else {
        content.add(new Image(dataUser.getAvatar()));
      }
      content.add(new HTML(dataUser.getFirstName() + " " + dataUser.getLastName()));

      mail.setText(dataUser.geteMail());
      if (dataUser.geteMail() == null || dataUser.geteMail().isEmpty()) {
        mail.setHTML("&nbsp");
      } else {
        mail.setText(dataUser.geteMail());
      }
    } else if (data instanceof GroupDTO) {
      content.setStylePrimaryName("group-name");
      GroupDTO dataGroup = (GroupDTO) data;
      content.add(new HTML(dataGroup.getName()));
    }
  }

  public BaseDTO getData() {
    return data;
  }

  public void hideData() {
    mail.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
    content.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
  }

  public boolean isSelected() {
    return selected;
  }

  @UiHandler("container")
  protected void selection(ClickEvent event) {
    toggleSelection();
    EventBus.getInstance().fireEvent(new ChangeEvent(this));
  }

  private void toggleSelection() {
    if (isSelected()) {
      container.getElement().removeAttribute("class");
      this.selected = false;
    } else {
      container.getElement().setAttribute("class", "selected");
      this.selected = true;
    }
  }
}
