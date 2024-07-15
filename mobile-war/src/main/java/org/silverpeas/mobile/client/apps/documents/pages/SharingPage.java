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

package org.silverpeas.mobile.client.apps.documents.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.apps.documents.events.app.DocumentsSharingEvent;
import org.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import org.silverpeas.mobile.client.apps.notifications.NotificationsApp;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.common.event.selection.AbstractSelectionPagesEvent;
import org.silverpeas.mobile.client.common.event.selection.SelectionPagesEventHandler;
import org.silverpeas.mobile.client.common.event.selection.UsersSelectionSendedEvent;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;
import org.silverpeas.mobile.shared.dto.tickets.TicketDTO;

public class SharingPage extends PageContent
    implements View, SelectionPagesEventHandler {

  private static SharingPageUiBinder uiBinder = GWT.create(SharingPageUiBinder.class);

  @UiField
  HTMLPanel container;

  @UiField
  ListBox validity;

  @UiField
  TextBox endValidity, maxAccess;

  @UiField
  TextArea comments, emails, users;

  @UiField
  Anchor submit;

  @UiField(provided = true)
  protected DocumentsMessages msg;
  interface SharingPageUiBinder extends UiBinder<Widget, SharingPage> {}

  private String contentType, contentId, instanceId;

  public SharingPage() {
    msg = GWT.create(DocumentsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("sharing");

    validity.addItem(msg.validityPermanent(), "0");
    validity.addItem(msg.validityTemporary(), "1");

    endValidity.getElement().setAttribute("type", "date");
    maxAccess.getElement().setAttribute("type", "number");

    container.getElementById("endValidityLabel").getStyle().setDisplay(Style.Display.NONE);
    container.getElementById("endValidityLabel").getStyle().setPaddingTop(1, Style.Unit.EM);
    container.getElementById("maxAccessLabel").getStyle().setDisplay(Style.Display.NONE);
    container.getElementById("maxAccessLabel").getStyle().setPaddingTop(1, Style.Unit.EM);
    container.getElementById("usersLabel").getStyle().setPaddingTop(1, Style.Unit.EM);

    EventBus.getInstance().addHandler(AbstractSelectionPagesEvent.TYPE, this);
  }

  public void setData(String contentType, String contentId, String instanceId) {
    this.instanceId = instanceId;
    this.contentId = contentId;
    this.contentType = contentType;
  }
  @Override
  public void onUsersSelected(UsersSelectionSendedEvent usersSelectionEvent) {
    String usersDisplay = "";
    String usersIds = "";
    for (BaseDTO selected : usersSelectionEvent.getSelection()) {
      if (selected instanceof UserDTO) {
        usersIds += ((UserDTO) selected).getId() + ",";
        usersDisplay += ((UserDTO) selected).getFirstName() + ((UserDTO) selected).getLastName() + " ";
      }
    }
    users.setText(usersDisplay);
    users.getElement().setAttribute("data", usersIds);
  }
  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractSelectionPagesEvent.TYPE, this);
  }

  @UiHandler("validity")
  protected void validityChange(ChangeEvent event) {
    if (validity.getSelectedValue().equals("1")) {
      endValidity.setVisible(true);
      container.getElementById("endValidityLabel").getStyle().setDisplay(Style.Display.BLOCK);
      if (contentType.equalsIgnoreCase("Attachment")) {
        maxAccess.setVisible(true);
        container.getElementById("maxAccessLabel").getStyle().setDisplay(Style.Display.BLOCK);
      }
    } else if (validity.getSelectedValue().equals("0")) {
      endValidity.setVisible(false);
      maxAccess.setVisible(false);
      container.getElementById("endValidityLabel").getStyle().setDisplay(Style.Display.NONE);
      container.getElementById("maxAccessLabel").getStyle().setDisplay(Style.Display.NONE);
    }
  }

  @UiHandler("submit")
  protected void share(ClickEvent event) {
    if (validity.getSelectedValue().equals("1") && endValidity.getText().isEmpty()) {
      Window.alert(msg.validityMandatory());
      return;
    }

    TicketDTO dto = new TicketDTO();
    dto.setValidity(validity.getSelectedValue());

    if (!endValidity.getText().isEmpty()) {
      String dt = endValidity.getText();
      String [] dtt = dt.split("-");
      dt = dtt[2] + "/" + dtt[1] + "/" + dtt[0];
      dto.setEndDateStr(dt);
    }
    dto.setComponentId(instanceId);
    dto.setSharedObjectType(contentType);
    dto.setSharedObjectId(contentId);
    dto.setNbAccessMax(maxAccess.getText());
    dto.setUsers(users.getElement().getAttribute("data"));
    dto.setAdditionalMessage(comments.getText());
    dto.setExternalEmails(emails.getText());
    EventBus.getInstance().fireEvent(new DocumentsSharingEvent(dto));
    back();
  }

  @UiHandler("users")
  protected void selectUsers(ClickEvent event) {
    NotificationsApp app = new NotificationsApp(null, null, null, "", pageTitle);
    app.setStandAlone(false);
    app.setUserOnly(true);
    app.start();
  }
}
