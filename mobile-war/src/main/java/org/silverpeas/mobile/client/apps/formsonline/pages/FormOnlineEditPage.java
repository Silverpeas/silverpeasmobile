/*
 * Copyright (C) 2000 - 2020 Silverpeas
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
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.formsonline.FormsOnlineApp;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormSaveEvent;
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
import org.silverpeas.mobile.client.components.Popin;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.forms.FieldEditable;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;

import java.util.ArrayList;
import java.util.List;

public class FormOnlineEditPage extends PageContent implements FormsOnlinePagesEventHandler {

  private static FormOnlineEditPageUiBinder uiBinder = GWT.create(FormOnlineEditPageUiBinder.class);

  private List<FormFieldDTO> data;

  @UiField(provided = true) protected FormsOnlineMessages msg = null;
  @UiField
  ActionsMenu actionsMenu;

  @UiField
  UnorderedList fields;

  @UiField
  Anchor validate, cancel;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private String instanceId;

  interface FormOnlineEditPageUiBinder extends UiBinder<Widget, FormOnlineEditPage> {
  }

  public FormOnlineEditPage() {
    msg = GWT.create(FormsOnlineMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    validate.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    cancel.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
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
  public void onFormLoaded(final FormLoadedEvent formLoadedEvent) {
    data = formLoadedEvent.getFormFields();
      for (FormFieldDTO f : formLoadedEvent.getFormFields()) {
        FieldEditable field = new FieldEditable();
        field.setData(f);
        fields.add(field);
      }
  }

  @Override
  public void onFormSaved(final FormSavedEvent formSavedEvent) {
    Notification.activityStop();
    stopAllFields();
    Notification.activityStop();
    back();
  }

  @Override
  public void onFormsOnlineRequestValidated(
      final FormsOnlineRequestValidatedEvent formsOnlineRequestValidatedEvent) {
  }

  @Override
  public void onFormRequestStatusChange(
      final FormRequestStatusChangedEvent formRequestStatusChangedEvent) {

  }

  @UiHandler("validate")
  protected void validate(ClickEvent event) {

    // manage mandatory fields
    ArrayList<String> errors = new ArrayList<String>();
    for (FormFieldDTO f : data) {
      if (f.isMandatory()) {
        if (f.getType().equalsIgnoreCase("file")) {
          if (f.getValueId() == null || f.getValueId().isEmpty()) {
            if (f.getObjectValue() == null) {
              errors.add(f.getLabel());
            }
          }
        } else if (FormsOnlineApp.isStoreValueId(f)) {
          if (f.getValueId() == null || f.getValueId().isEmpty()) {
            errors.add(f.getLabel());
          }
        } else {
          if (f.getValue() == null || f.getValue().isEmpty()) {
            errors.add(f.getLabel());
          }
        }
      }
    }
    if (!errors.isEmpty()) {
      String message = "";
      for (String error : errors) {
        message += error + ",";
      }
      message = message.substring(0, message.length() - 1) + " ";
      if (errors.size() == 1) {
        message += msg.mandatoryOneField();
      } else {
        message += msg.mandatory();
      }

      new Popin(message).show();
    } else {
      Notification.activityStart();
      FormSaveEvent ev = new FormSaveEvent();
      ev.setData(data);
      EventBus.getInstance().fireEvent(ev);
    }
  }

  @UiHandler("cancel")
  protected void cancel(ClickEvent event) {
    stopAllFields();
    back();
  }
  private void stopAllFields() {
    for (int i = 0; i < fields.getWidgetCount(); i++) {
      ((FieldEditable) fields.getWidget(i)).stop();
    }
  }
}