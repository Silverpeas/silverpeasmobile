/*
 * Copyright (C) 2000 - 2025 Silverpeas
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
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.SpMobil;
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
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.forms.FieldViewable;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormRequestDTO;
import org.silverpeas.mobile.shared.dto.formsonline.ValidationRequestDTO;

public class FormOnlineViewPage extends PageContent implements FormsOnlinePagesEventHandler {

  private static FormOnlineEditPageUiBinder uiBinder = GWT.create(FormOnlineEditPageUiBinder.class);

  private boolean hasHtmlLayer;

  @UiField(provided = true) protected FormsOnlineMessages msg = null;
  @UiField
  UnorderedList fields;

  @UiField
  static HTML layer;

  @UiField
  TextArea comment;

  @UiField
  Anchor accept, reject, cancel;

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

  @UiHandler("cancel")
  protected void cancel(ClickEvent event) {
    Notification.activityStart();
    ValidationRequestDTO validation = new ValidationRequestDTO();
    validation.setDecision("cancel");
    validation.setComment(comment.getText());
    EventBus.getInstance().fireEvent(new FormsOnlineValidationRequestEvent(data, validation));
  }

  public void setData(FormRequestDTO data) {
    this.data = data;
    reject.setVisible(data.isReadOnly());
    accept.setVisible(data.isReadOnly());
    cancel.setVisible(!data.isReadOnly());
    if (data.getState() == FormRequestDTO.STATE_CANCELED) cancel.setVisible(false);

    labelComment.setVisible(data.isReadOnly());
    comment.setVisible(data.isReadOnly());

    if (data.getHtmlLayer() != null && data.getHtmlLayer().getHtml() != null && !data.getHtmlLayer().getHtml().isEmpty()
            && ResourcesManager.getParam("form.htmllayer.display").equalsIgnoreCase("true")) {
      hasHtmlLayer = true;
      String html = data.getHtmlLayer().getHtml();
      for (FormFieldDTO f : data.getData()) {
        if (f.getValue() == null || f.getValue().equalsIgnoreCase("null")) f.setValue("");
        if (f.getType().equalsIgnoreCase("file") && !f.getValue().isEmpty()) {
            html = html.replaceFirst("<%=" + f.getName() + "%>",
                    "<a class='downloadable' href='/silverpeas/attached_file/componentId/" +
                            getApp().getApplicationInstance().getId() + "/attachmentId/" +
                            f.getValueId() + "/lang/" + SpMobil.getUser().getLanguage() + "/name/" + f.getValue() +
                            "' target='_self' download='" + f.getValue() + "'>" + f.getValue() + "</a>");
        } else {
          html = html.replaceFirst("<%=" + f.getName() + "%>", f.getValue());
        }
      }
      layer.setHTML(html);
    } else {
      hasHtmlLayer = false;
      for (FormFieldDTO f : data.getData()) {
        FieldViewable item = new FieldViewable();
        item.setData(f);
        fields.add(item);
      }
    }
    ScriptInjector.fromUrl("/weblib/xmlForms/" + data.getFormName().replace(".xml",".js")).setWindow(ScriptInjector.TOP_WINDOW).inject();
  }
}