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

package com.silverpeas.mobile.client.apps.workflow.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadUserFieldEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.userselection.UserSelectionPage;
import com.silverpeas.mobile.client.components.userselection.events.components
    .AbstractUserSelectionComponentEvent;
import com.silverpeas.mobile.client.components.userselection.events.components
    .UserSelectionComponentEventHandler;
import com.silverpeas.mobile.client.components.userselection.events.components
    .UsersAndGroupsSelectedEvent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.UserDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowFieldDTO;

import java.util.Map;

public class FieldEditable extends Composite implements ChangeHandler, ValueChangeHandler,
    UserSelectionComponentEventHandler, View {

  private static FieldUiBinder uiBinder = GWT.create(FieldUiBinder.class);

  @UiField
  Label label;
  @UiField
  HTMLPanel container;

  private Widget w;
  protected ApplicationMessages msg = null;
  private WorkflowFieldDTO data;

  @Override
  public void onChange(final ChangeEvent changeEvent) {
    String value = "";
    if (getType().equals("user")) {
      //TODO
    } else {
      if (changeEvent.getSource() instanceof TextBox) {
        value = ((TextBox) changeEvent.getSource()).getText();
      } else if (changeEvent.getSource() instanceof TextArea) {
        value = ((TextArea) changeEvent.getSource()).getText();
      } else if (changeEvent.getSource() instanceof ListBox) {
        value = ((ListBox) changeEvent.getSource()).getSelectedValue();
      }
      //TODO : file ?
    }
    data.setValue(value);
  }

  @Override
  public void onValueChange(final ValueChangeEvent valueChangeEvent) {
    String value = ((RadioButton) valueChangeEvent.getSource()).getName();
    data.setValue(value);
  }

  @Override
  public void onUsersAndGroupSelected(final UsersAndGroupsSelectedEvent event) {
    if (event.getContentId().equals(data.getName()) ) {
      ListBox l = ((ListBox) w);
      l.clear();
      l.setVisibleItemCount(event.getUsersAndGroupsSelected().size());
      for (BaseDTO user : event.getUsersAndGroupsSelected()) {
        UserDTO u = (UserDTO) user;
        l.addItem(u.getFirstName() + " " + u.getLastName(), u.getId());
      }
    }
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler( AbstractUserSelectionComponentEvent.TYPE, this);
  }

  interface FieldUiBinder extends UiBinder<Widget, FieldEditable> {}

  public FieldEditable() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(AbstractUserSelectionComponentEvent.TYPE, this);
  }

  private String getType() {
    String type = data.getDisplayerName();
    if (type == null|| type.isEmpty()) type = data.getType();
    return type;
  }

  public void setData(WorkflowFieldDTO data) {
    this.data = data;
    label.setText(data.getLabel());
    String type = getType();
    if (type.equalsIgnoreCase("textarea")) {
      TextArea t = new TextArea();
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
      t.addChangeHandler(this);
      w = t;
    }  else if(type.equalsIgnoreCase("text")) {
      TextBox t = new TextBox();
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
      t.addChangeHandler(this);
      w = t;
    }  else if(type.equalsIgnoreCase("radio")) {
      FlowPanel panel = new FlowPanel();
      panel.getElement().getStyle().setDisplay(Style.Display.INLINE);
      for(Map.Entry<String,String> v : data.getValues().entrySet()) {
        RadioButton rb0 = new RadioButton(v.getKey(),v.getValue());
        rb0.addValueChangeHandler(this);
        panel.add(rb0);
      }
      w = panel;

    } else if(type.equalsIgnoreCase("listbox")) {
      ListBox l = new ListBox();
      for(Map.Entry<String,String> v : data.getValues().entrySet()) {
        l.addItem(v.getKey(),v.getValue());
      }
      l.setEnabled(!data.isReadOnly());
      l.addChangeHandler(this);
      data.setValue(l.getSelectedValue());
      w = l;
    } else if(type.equalsIgnoreCase("file")) {
      TextBox t = new TextBox();
      t.getElement().setAttribute("type", "file");
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
      t.addChangeHandler(this);
      w = t;
    } else if(type.equalsIgnoreCase("date")) {
      TextBox t = new TextBox();
      t.getElement().setAttribute("type", "date");
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
      t.addChangeHandler(this);
      w = t;
    } else if(type.equalsIgnoreCase("user")) {
      ListBox l = new ListBox();
      if (data.getValue() != null && !data.getValue().isEmpty()) {
        l.addItem(data.getValue(), data.getValue());
      }
      l.setWidth("10em");
      l.setEnabled(!data.isReadOnly());
      l.addChangeHandler(this);
      l.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(final ClickEvent clickEvent) {
          UserSelectionPage page = new UserSelectionPage();
          page.setContentId(data.getName());
          WorkflowLoadUserFieldEvent event = new WorkflowLoadUserFieldEvent();
          event.setFieldName(data.getName());
          event.setInstanceId(data.getId());
          EventBus.getInstance().fireEvent(event);
          page.show();
        }
      });

      w = l;
    } else if(type.equalsIgnoreCase("url")) {
      TextBox t = new TextBox();
      t.getElement().setAttribute("type", "url");
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
      t.addChangeHandler(this);
      w = t;
    }
    container.add(w);
    if (data.isMandatory()) {
      Image im = new Image();
      im.setStylePrimaryName("mandatory");
      container.add(im);
    }
  }

  public WorkflowFieldDTO getData() {
    return data;
  }

}
