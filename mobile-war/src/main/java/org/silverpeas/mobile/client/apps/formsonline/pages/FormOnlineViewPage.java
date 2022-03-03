/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.client.apps.formsonline.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormsOnlineValidationRequestEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.AbstractFormsOnlinePagesEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormLoadedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormRequestStatusChangedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormSavedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormsOnlineLoadedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormsOnlinePagesEventHandler;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormsOnlineRequestValidatedEvent;
import org.silverpeas.mobile.client.apps.formsonline.resources.FormsOnlineMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.forms.FieldViewable;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormRequestDTO;
import org.silverpeas.mobile.shared.dto.formsonline.ValidationRequestDTO;

public class FormOnlineViewPage extends PageContent implements FormsOnlinePagesEventHandler {

  private static FormOnlineEditPageUiBinder uiBinder = GWT.create(FormOnlineEditPageUiBinder.class);

  @UiField(provided = true) protected FormsOnlineMessages msg = null;
  @UiField
  ActionsMenu actionsMenu;

  @UiField
  UnorderedList fields;

  @UiField
  TextArea comment;

  @UiField
  Anchor accept, reject;

  @UiField
  Label labelComment;

  private String instanceId;
  private FormRequestDTO data;

  interface FormOnlineEditPageUiBinder extends UiBinder<Widget, FormOnlineViewPage> {
  }

  public FormOnlineViewPage() {
    msg = GWT.create(FormsOnlineMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    accept.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    reject.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    EventBus.getInstance().addHandler(AbstractFormsOnlinePagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractFormsOnlinePagesEvent.TYPE, this);
  }

  @Override
  public void onFormsOnlineLoad(final FormsOnlineLoadedEvent event) {}

  @Override
  public void onFormLoaded(final FormLoadedEvent formLoadedEvent) {}

  @Override
  public void onFormSaved(final FormSavedEvent formSavedEvent) {}

  @Override
  public void onFormsOnlineRequestValidated(
      final FormsOnlineRequestValidatedEvent formsOnlineRequestValidatedEvent) {
    stop();
    back();
    Notification.activityStop();
  }

  @Override
  public void onFormRequestStatusChange(
      final FormRequestStatusChangedEvent formRequestStatusChangedEvent) {

  }

  @UiHandler("accept")
  protected void validate(ClickEvent event) {
    Notification.activityStart();
    ValidationRequestDTO validation = new ValidationRequestDTO();
    validation.setDecision("validate");
    validation.setComment(comment.getText());
    EventBus.getInstance().fireEvent(new FormsOnlineValidationRequestEvent(data, validation));
  }

  @UiHandler("reject")
  protected void reject(ClickEvent event) {
    Notification.activityStart();
    ValidationRequestDTO validation = new ValidationRequestDTO();
    validation.setDecision("refuse");
    validation.setComment(comment.getText());
    EventBus.getInstance().fireEvent(new FormsOnlineValidationRequestEvent(data, validation));
  }

  public void setData(FormRequestDTO data, boolean readOnly) {
    this.data = data;
    reject.setVisible(!readOnly);
    accept.setVisible(!readOnly);
    labelComment.setVisible(!readOnly);
    comment.setVisible(!readOnly);

    for (FormFieldDTO f : data.getData()) {
      FieldViewable item = new FieldViewable();
      item.setData(f);
      fields.add(item);
    }
  }
}