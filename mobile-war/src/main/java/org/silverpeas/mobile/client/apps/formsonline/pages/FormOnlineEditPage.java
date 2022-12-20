/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.client.apps.formsonline.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormOnlineLoadUserFieldEvent;
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
import org.silverpeas.mobile.client.common.FormsHelper;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.components.Popin;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.forms.FieldEditable;
import org.silverpeas.mobile.client.components.userselection.UserSelectionPage;
import org.silverpeas.mobile.client.components.userselection.events.components.AbstractUserSelectionComponentEvent;
import org.silverpeas.mobile.client.components.userselection.events.components.UserSelectionComponentEventHandler;
import org.silverpeas.mobile.client.components.userselection.events.components.UsersAndGroupsSelectedEvent;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;
import org.silverpeas.mobile.shared.dto.GroupDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormOnlineEditPage extends PageContent implements UserSelectionComponentEventHandler, FormsOnlinePagesEventHandler {

  private static FormOnlineEditPageUiBinder uiBinder = GWT.create(FormOnlineEditPageUiBinder.class);

  private static List<FormFieldDTO> data;

  @UiField(provided = true) protected FormsOnlineMessages msg = null;
  @UiField
  ActionsMenu actionsMenu;

  @UiField
  UnorderedList fields;

  @UiField
  static HTML layer;

  @UiField
  Anchor validate, cancel;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private String instanceId;

  private boolean hasHtmlLayer = false;

  interface FormOnlineEditPageUiBinder extends UiBinder<Widget, FormOnlineEditPage> {
  }

  public FormOnlineEditPage() {
    exportGWTFunction();
    msg = GWT.create(FormsOnlineMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    validate.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    cancel.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    EventBus.getInstance().addHandler(AbstractFormsOnlinePagesEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractUserSelectionComponentEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractFormsOnlinePagesEvent.TYPE, this);
    EventBus.getInstance().removeHandler(AbstractUserSelectionComponentEvent.TYPE, this);
  }

  @Override
  public void onFormsOnlineLoad(final FormsOnlineLoadedEvent event) {}

  @Override
  public void onFormLoaded(final FormLoadedEvent formLoadedEvent) {
    if (formLoadedEvent.getHtml() != null && !formLoadedEvent.getHtml().isEmpty()
            && ResourcesManager.getParam("form.htmllayer.display").equalsIgnoreCase("true")) {
      hasHtmlLayer = true;
      data = formLoadedEvent.getFormFields();
      layer.setHTML(formLoadedEvent.getHtml());
      //ScriptInjector.fromString("alert('run');").inject();
    } else {
      hasHtmlLayer = false;
      data = formLoadedEvent.getFormFields();
      for (FormFieldDTO f : formLoadedEvent.getFormFields()) {
        FieldEditable field = new FieldEditable();
        field.setData(f);
        fields.add(field);
      }
    }
  }

  @Override
  public void onUsersAndGroupSelected(final UsersAndGroupsSelectedEvent event) {
    if (hasHtmlLayer) {
      TextAreaElement t = getUserField(event.getContentId());
      String value = "";
        t.setRows(event.getUsersAndGroupsSelected().size());
        t.setValue("");
        for (BaseDTO dto : event.getUsersAndGroupsSelected()) {
          if (dto instanceof UserDTO) {
            UserDTO u = (UserDTO) dto;
            t.setValue(t.getValue() + u.getFirstName() + " " + u.getLastName() + "\n");
            value = value + u.getId() + ",";
          } else if (dto instanceof GroupDTO) {
            GroupDTO g = (GroupDTO) dto;
            t.setValue(g.getName());
            value = value + g.getId() + ",";
          }
        }
        if (!value.isEmpty()) {
          value = value.substring(0, value.length() - 1);
        }
        t.setAttribute("data", value);

        // update model
        for (FormFieldDTO f : data) {
          if (f.getName().equals(event.getContentId())) {
            f.setValueId(value);
          }
        }
    }
  }

  private static TextAreaElement getUserField(String fieldName) {
    TextAreaElement t = null;
    NodeList<Element> l = layer.getElement().getElementsByTagName("textarea");
    for (int i = 0; i < l.getLength(); i++) {
      if (l.getItem(i).getId().equals(fieldName)) {
        t = TextAreaElement.as(l.getItem(i));
        break;
      }
    }
    return t;
  }

  private static String getFieldValue(String fieldId) {
    String value = "";
    Element el = Document.get().getElementById(fieldId);
    String tagName = el.getTagName();
    if (tagName.equalsIgnoreCase("input")) {
      value = InputElement.as(el).getValue();
    } else if (tagName.equalsIgnoreCase("select")) {
      value = SelectElement.as(el).getValue();
    } else if (tagName.equalsIgnoreCase("textarea")) {
      value = TextAreaElement.as(el).getValue();
    }
    return value;
  }

  private static boolean isCheckbox(String fieldId) {
    Element el = Document.get().getElementById(fieldId);
    String tagName = el.getTagName();
    if (tagName.equalsIgnoreCase("input")) {
      return el.getAttribute("type").equalsIgnoreCase("checkbox");
    }
    return false;
  }

  public static native void exportGWTFunction()/*-{
    $wnd.showUserSelection = function(fieldName, type) {
      @org.silverpeas.mobile.client.apps.formsonline.pages.FormOnlineEditPage::showUserSelection(*)(fieldName, type);
    }
    $wnd.updateModel = function(fieldId, fieldName) {
      @org.silverpeas.mobile.client.apps.formsonline.pages.FormOnlineEditPage::updateModel(*)(fieldId, fieldName);
    }
  }-*/;

  public static boolean isStoreValueId(String fieldId, String fieldName) {
    Element el = Document.get().getElementById(fieldId);
    String tagName = el.getTagName();
    if (tagName.equalsIgnoreCase("input")) {
      return el.getAttribute("type").equalsIgnoreCase("radio");
    } else if (tagName.equalsIgnoreCase("select")) {
      return true;
    }
    return false;
  }

  public static void updateModel(String fieldId, String fieldName) {
    for (FormFieldDTO f : data) {
      if (f.getName().equals(fieldName)) {
        if (isCheckbox(fieldId)) {
          Element el = Document.get().getElementById(fieldId).getParentElement();
          NodeList<Node> nodes = el.getChildNodes();
          String value = "";
          for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.getItem(i).getNodeName().equalsIgnoreCase("input")) {
              Element e = (Element) nodes.getItem(i);
              if (e.getPropertyBoolean("checked")) {
                if (value.isEmpty()) {
                  value = e.getAttribute("value");
                } else {
                  value += "##" + e.getAttribute("value");
                }
              }
            }
          }
          f.setValueId(value);
        } else if (isStoreValueId(fieldId, fieldName)) {
          f.setValueId(getFieldValue(fieldId));
        } else {
          Element el = Document.get().getElementById(fieldId);
          if (el.getTagName().equalsIgnoreCase("input") && el.getAttribute("type").equalsIgnoreCase("file")) {
            f.setObjectValue(el);
          } else {
            f.setValue(getFieldValue(fieldId));
          }
        }
        break;
      }
    }
  }

  public static void showUserSelection(String fieldName, String type) {

    UserSelectionPage page = new UserSelectionPage();
    if (type.equalsIgnoreCase("user") || type.equalsIgnoreCase("group")) page.setMaxSelection(1);
    page.setContentId(fieldName);

    // get users or groups selected before
    TextAreaElement tx = getUserField(fieldName);
    List<String> ids = Arrays.asList(tx.getAttribute("data").split(","));
    page.setPreSelectedIds(ids);
    sendEventToGetPossibleUsers(fieldName);
    page.show();
  }

  protected static void sendEventToGetPossibleUsers(String fieldName) {
    FormOnlineLoadUserFieldEvent event = new FormOnlineLoadUserFieldEvent();
    event.setFieldName(fieldName);
    EventBus.getInstance().fireEvent(event);
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
    ArrayList<String> errors = new ArrayList<String>();
    if (!hasHtmlLayer) {
      // manage mandatory fields
      for (FormFieldDTO f : data) {
        if (f.isMandatory()) {
          if (f.getType().equalsIgnoreCase("file")) {
            if (f.getValueId() == null || f.getValueId().isEmpty()) {
              if (f.getObjectValue() == null) {
                errors.add(f.getLabel());
              }
            }
          } else if (FormsHelper.isStoreValueId(f)) {
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