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

package org.silverpeas.mobile.client.apps.workflow.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowProcessFormEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.AbstractWorkflowPagesEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowActionProcessedEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowLoadedDataEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowLoadedInstancesEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowPagesEventHandler;
import org.silverpeas.mobile.client.apps.workflow.pages.widgets.WorkflowFieldEditable;
import org.silverpeas.mobile.client.apps.workflow.resources.WorkflowMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.FormsHelper;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.components.Snackbar;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowFieldDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowFormActionDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancePresentationFormDTO;

import java.util.ArrayList;

public class WorkflowActionFormPage extends PageContent implements WorkflowPagesEventHandler {

  private static WorkflowPresentationPageUiBinder
      uiBinder = GWT.create(WorkflowPresentationPageUiBinder.class);

  private WorkflowFormActionDTO data;
  @UiField WorkflowMessages msg;

  @UiField
  UnorderedList fields;
  @UiField HeadingElement header;
  @UiField Anchor validate, cancel;

  public void setData(final WorkflowInstancePresentationFormDTO data) {
  }

  public void setData(final WorkflowFormActionDTO data, final ApplicationInstanceDTO instance) {
    this.data = data;
    header.setInnerText(data.getTitle());
    for (WorkflowFieldDTO field : data.getFields()) {
      WorkflowFieldEditable f = new WorkflowFieldEditable();
      f.setData(field);
      fields.add(f);
    }
  }

  @Override
  public void loadDataInstances(final WorkflowLoadedDataEvent event) {

  }

  @Override
  public void loadInstances(final WorkflowLoadedInstancesEvent event) {
  }

  @Override
  public void actionProcessed(final WorkflowActionProcessedEvent event) {
    stopAllFields();
    Notification.activityStop();
    back();
    if (!data.getActionName().equalsIgnoreCase("create")) {
      back();
    }
  }

  interface WorkflowPresentationPageUiBinder extends UiBinder<Widget, WorkflowActionFormPage> {
  }

  public WorkflowActionFormPage() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(WorkflowMessages.class);
    validate.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    cancel.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    EventBus.getInstance().addHandler(AbstractWorkflowPagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractWorkflowPagesEvent.TYPE, this);
    super.stop();
  }

  @UiHandler("validate")
  protected void validate(ClickEvent event) {

    // manage mandatory fields
    ArrayList<String> errors = new ArrayList<String>();
    for (WorkflowFieldDTO f : data.getFields()) {
      if (f.isMandatory()) {
        if (f.getType().equalsIgnoreCase("file")) {
          if (f.getObjectValue() == null) {
            errors.add(f.getLabel());
          }
        } else if(FormsHelper.isStoreValueId(f)) {
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

      Snackbar.show(message, Snackbar.DELAY, Snackbar.INFO);
    } else {
      Notification.activityStart();
      WorkflowProcessFormEvent ev = new WorkflowProcessFormEvent();
      ev.setProcessId(data.getId());
      ev.setData(data.getFields());
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
      ((WorkflowFieldEditable) fields.getWidget(i)).stop();
    }
  }

}