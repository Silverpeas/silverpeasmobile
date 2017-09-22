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

package com.silverpeas.mobile.client.apps.workflow.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.workflow.pages.widgets.FieldEditable;
import com.silverpeas.mobile.client.apps.workflow.resources.WorkflowMessages;
import com.silverpeas.mobile.client.components.Popin;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.ActionsMenu;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowFieldDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowFormActionDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowInstancePresentationFormDTO;

import java.util.ArrayList;

public class WorkflowActionFormPage extends PageContent {

  private static WorkflowPresentationPageUiBinder
      uiBinder = GWT.create(WorkflowPresentationPageUiBinder.class);

  private WorkflowFormActionDTO data;
  private WorkflowMessages msg;

  @UiField UnorderedList fields;
  @UiField ActionsMenu actionsMenu;
  @UiField HeadingElement header;
  @UiField Anchor validate, cancel;

  public void setData(final WorkflowInstancePresentationFormDTO data) {

  }

  public void setData(final WorkflowFormActionDTO data, final ApplicationInstanceDTO instance) {
    this.data = data;
    header.setInnerText(data.getTitle());
    for (WorkflowFieldDTO field : data.getFields()) {
      FieldEditable f = new FieldEditable();
      f.setData(field);
      fields.add(f);
    }

  }

  interface WorkflowPresentationPageUiBinder extends UiBinder<Widget, WorkflowActionFormPage> {
  }

  public WorkflowActionFormPage() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(WorkflowMessages.class);
    validate.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    cancel.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
  }

  @Override
  public void stop() {
    super.stop();
  }

  @UiHandler("validate")
  protected void validate(ClickEvent event) {

    //TODO : manage mandatory
    ArrayList<String> errors = new ArrayList<String>();
    for (WorkflowFieldDTO f : data.getFields()) {
      if (f.isMandatory()) {
        if (f.getValue() == null || f.getValue().isEmpty()) {
          errors.add(f.getLabel());
        }
      }
    }
    if (!errors.isEmpty()) {
      String message = "";
      for (String error : errors) {
        message += error + ",";
      }
      message = message.substring(0, message.length() - 2) + " ";
      message += msg.mandatory();

      new Popin(message).show();
    }

    //EventBus.getInstance().fireEvent();
    //stopAllFields();
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