package com.silverpeas.mobile.client.apps.notifications.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.silverpeas.mobile.client.apps.notifications.pages.widgets.events.ChangeEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.components.HTMLPanelClickable;
import com.silverpeas.mobile.client.resources.ApplicationResources;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.GroupDTO;
import com.silverpeas.mobile.shared.dto.UserDTO;

public class UserGroupItem extends Composite {

  private static ContactItemUiBinder uiBinder = GWT.create(ContactItemUiBinder.class);
  @UiField HTML mail;
  @UiField HTMLPanel content;
  @UiField HTMLPanelClickable container;

  private BaseDTO data = null;
  private boolean selected = false;

  private ApplicationResources resources = GWT.create(ApplicationResources.class);

  interface ContactItemUiBinder extends UiBinder<Widget, UserGroupItem> {
  }

  public UserGroupItem() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setData(BaseDTO data) {
    this.data = data;
    if (data instanceof UserDTO) {
      UserDTO dataUser = (UserDTO) data;
      if (dataUser.getAvatar() == null) {
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
    if (isSelected()) {
      container.getElement().removeAttribute("class");
      this.selected = false;
    } else {
      container.getElement().setAttribute("class", "selected");
      this.selected = true;
    }

    EventBus.getInstance().fireEvent(new ChangeEvent(selected));
  }
}
