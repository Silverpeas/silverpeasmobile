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
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadUserFieldEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.navigation.UrlUtils;
import com.silverpeas.mobile.client.components.userselection.UserSelectionPage;
import com.silverpeas.mobile.client.components.userselection.events.components.AbstractUserSelectionComponentEvent;
import com.silverpeas.mobile.client.components.userselection.events.components.UserSelectionComponentEventHandler;
import com.silverpeas.mobile.client.components.userselection.events.components.UsersAndGroupsSelectedEvent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.GroupDTO;
import com.silverpeas.mobile.shared.dto.UserDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowFieldDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FieldEditable extends Composite implements ChangeHandler, ValueChangeHandler,
    UserSelectionComponentEventHandler, View {

  private static FieldUiBinder uiBinder = GWT.create(FieldUiBinder.class);

  @UiField Label label;
  @UiField Anchor file, deleteFile;
  @UiField HTMLPanel container, inputContainer;

  private Widget w;
  protected ApplicationMessages msg = null;
  private WorkflowFieldDTO data;

  @Override
  public void onChange(final ChangeEvent changeEvent) {
    String value = "";
    if (changeEvent.getSource() instanceof TextBox) {
      if (((TextBox) changeEvent.getSource()).getElement().getAttribute("type").equalsIgnoreCase("file")) {
        data.setObjectValue(((TextBox) changeEvent.getSource()).getElement());
      } else {
        value = ((TextBox) changeEvent.getSource()).getText();
      }
    } else if (changeEvent.getSource() instanceof TextArea) {
      value = ((TextArea) changeEvent.getSource()).getText();
    } else if (changeEvent.getSource() instanceof ListBox) {
      value = ((ListBox) changeEvent.getSource()).getSelectedValue();
    }
    data.setValue(value);
  }

  @Override
  public void onValueChange(final ValueChangeEvent valueChangeEvent) {
    String value = "";
    if (valueChangeEvent.getSource() instanceof RadioButton) {
      value = ((RadioButton) valueChangeEvent.getSource()).getText();
    } else if(valueChangeEvent.getSource() instanceof CheckBox) {
      FlowPanel p = (FlowPanel) ((CheckBox) valueChangeEvent.getSource()).getParent();
      for (int i = 0; i < p.getWidgetCount(); i++) {
        CheckBox c = (CheckBox) p.getWidget(i);
        if (c.getValue()) {
          value += c.getName() + ",";
        }
      }
      if (value.indexOf(",") != -1) {
        value = value.substring(0, value.length() - 1);
      }
    }
    data.setValue(value);
  }

  @Override
  public void onUsersAndGroupSelected(final UsersAndGroupsSelectedEvent event) {
    if (event.getContentId().equals(data.getName()) ) {
      String value = "";
      ListBox l = ((ListBox) w);
      l.clear();
      l.setVisibleItemCount(event.getUsersAndGroupsSelected().size() + 1);
      for (BaseDTO dto : event.getUsersAndGroupsSelected()) {
        if (dto instanceof UserDTO) {
          UserDTO u = (UserDTO) dto;
          l.addItem(u.getFirstName() + " " + u.getLastName(), u.getId());
          value = value + u.getId() + ",";
        } else if (dto instanceof GroupDTO) {
          GroupDTO g = (GroupDTO) dto;
          l.addItem(g.getName(), g.getId());
          value = value + g.getId() + ",";
        }
      }
      if (!value.isEmpty()) {
        value = value.substring(0, value.length()-1);
      }
      data.setValueId(value);
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

  //TODO : manage displayer : wysiwyg, sequence, jdbc, ldap, accessPath, pdc, explorer
  public void setData(WorkflowFieldDTO data) {
    this.data = data;
    label.setText(data.getLabel());
    String type = getType();
    if (type.equalsIgnoreCase("textarea") || type.equalsIgnoreCase("wysiwyg")) {
      TextArea t = new TextArea();
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
      t.addChangeHandler(this);
      w = t;
    }  else if(type.equalsIgnoreCase("text") || type.equalsIgnoreCase("simpletext")) {
      TextBox t = new TextBox();
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
      t.addChangeHandler(this);
      w = t;
    }  else if(type.equalsIgnoreCase("radio")) {
      FlowPanel panel = new FlowPanel();
      panel.getElement().getStyle().setDisplay(Style.Display.INLINE);
      for(Map.Entry<String,String> v : data.getValues().entrySet()) {
        RadioButton rb0 = new RadioButton(data.getName(), v.getValue());
        rb0.addValueChangeHandler(this);
        if (data.getValue() != null && data.getValue().equals(v.getValue())) {
          rb0.setValue(true);
        }
        panel.add(rb0);
      }
      w = panel;
    /*} else if(type.equalsIgnoreCase("checkbox")) {
      FlowPanel panel = new FlowPanel();
      panel.getElement().getStyle().setDisplay(Style.Display.INLINE);
      for(Map.Entry<String,String> v : data.getValues().entrySet()) {
        CheckBox chk = new CheckBox(v.getLabel());
        chk.setName(v.getValue());
        chk.addValueChangeHandler(this);
        panel.add(chk);
      }
      w = panel;*/
    } else if(type.equalsIgnoreCase("listbox")) {
      ListBox l = new ListBox();
      int i = 0;
      int index = 0;
      for(Map.Entry<String,String> v : data.getValues().entrySet()) {
        l.addItem(v.getKey(),v.getValue());
        if (data.getValue() != null && v.getValue().equals(data.getValue())) {
          i = index;
        }
        index++;
      }
      l.setEnabled(!data.isReadOnly());
      l.addChangeHandler(this);
      l.setSelectedIndex(i);
      data.setValue(l.getSelectedValue());
      w = l;
    } else if(type.equalsIgnoreCase("file")) {
      TextBox t = new TextBox();
      t.getElement().setAttribute("type", "file");
      t.setReadOnly(data.isReadOnly());
      t.addChangeHandler(this);
      w = t;

      if (data.getValue() != null && !data.getValue().isEmpty()) {
        file.setText(data.getValue());
        label.getElement().getStyle().setDisplay(Style.Display.INLINE);
        String url = UrlUtils.getServicesLocation();
        url += "Attachment";
        url = url + "?id=" + data.getValueId() + "&lang=" + SpMobil.user.getLanguage();
        file.setHref(url);
        file.setTarget("_self");
        file.getElement().setAttribute("download", data.getValue());
        file.setVisible(true);
        deleteFile.setVisible(true);
      }
    } else if(type.equalsIgnoreCase("date")) {
      TextBox t = new TextBox();
      t.getElement().setAttribute("type", "date");
      t.getElement().setAttribute("value", data.getValue());
      t.setReadOnly(data.isReadOnly());
      t.addChangeHandler(this);
      w = t;
    } else if(type.equalsIgnoreCase("time")) {
      TextBox t = new TextBox();
      t.getElement().setAttribute("type", "time");
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
      t.addChangeHandler(this);
      w = t;
    } else if(type.equalsIgnoreCase("email")) {
        TextBox t = new TextBox();
        t.getElement().setAttribute("type", "email");
        t.setText(data.getValue());
        t.setReadOnly(data.isReadOnly());
        t.addChangeHandler(this);
        w = t;
    } else if(type.equalsIgnoreCase("user") || type.equalsIgnoreCase("multipleUser") || type.equalsIgnoreCase("group")) {
      ListBox l = new ListBox();
      if (type.equalsIgnoreCase("multipleUser")) {
        if (data.getValue() != null && data.getValueId() != null && !data.getValue().isEmpty() && !data.getValueId().isEmpty()) {
          String[] values = data.getValue().split(",");
          String[] ids = data.getValueId().split(",");
          for (int i = 0; i < values.length; i++) {
            l.addItem(values[i], ids[i].trim());
          }
        }
      } else {
        if (data.getValue() != null && !data.getValue().isEmpty()) {
          l.addItem(data.getValue(), data.getValueId());
        }
      }
      l.setWidth("10em");
      l.setEnabled(!data.isReadOnly());
      l.setVisibleItemCount(2);
      l.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(final ClickEvent clickEvent) {
          UserSelectionPage page = new UserSelectionPage();
          if (type.equalsIgnoreCase("user") || type.equalsIgnoreCase("group")) page.setMaxSelection(1);
          page.setContentId(data.getName());

          // get users or groups selected before
          ListBox ls = ((ListBox) clickEvent.getSource());
          List<String> ids = new ArrayList<String>();
          for (int i = 0; i < ls.getItemCount(); i++) {
            ids.add(ls.getValue(i));
          }
          page.setPreSelectedIds(ids);

          WorkflowLoadUserFieldEvent event = new WorkflowLoadUserFieldEvent();
          event.setFieldName(data.getName());
          event.setInstanceId(data.getId());
          event.setActionName(data.getActionName());
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
    inputContainer.add(w);
    if (data.isMandatory()) {
      Image im = new Image();
      im.setStylePrimaryName("mandatory");
      inputContainer.add(im);
    }
  }

  public WorkflowFieldDTO getData() {
    return data;
  }

  @UiHandler("deleteFile")
  void executeAction(ClickEvent event){
    data.setValue(null);
    data.setObjectValue(null);
    data.setValueId(null);
    file.setText("");
    file.setVisible(false);
    deleteFile.setVisible(false);
  }

}
