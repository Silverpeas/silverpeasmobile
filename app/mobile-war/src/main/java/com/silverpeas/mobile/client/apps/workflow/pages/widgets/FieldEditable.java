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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowFieldDTO;

import java.util.Map;

public class FieldEditable extends Composite {

  private static FieldUiBinder uiBinder = GWT.create(FieldUiBinder.class);

  @UiField
  Label label;
  @UiField
  HTMLPanel container;
  protected ApplicationMessages msg = null;

  interface FieldUiBinder extends UiBinder<Widget, FieldEditable> {}

  public FieldEditable() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void setData(WorkflowFieldDTO data) {
    label.setText(data.getLabel());
    Widget w = null;
    String type = data.getDisplayerName();
    if (type == null|| type.isEmpty()) type = data.getType();
    if (type.equalsIgnoreCase("textarea")) {
      TextArea t = new TextArea();
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
      w = t;
    }  else if(type.equalsIgnoreCase("text")) {
      TextBox t = new TextBox();
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
      w = t;
    }  else if(type.equalsIgnoreCase("radio")) {
      FlowPanel panel = new FlowPanel();
      for(Map.Entry<String,String> v : data.getValues().entrySet()) {
        RadioButton rb0 = new RadioButton(v.getKey(),v.getValue());
        panel.add(rb0);
      }
      w = panel;

    } else if(type.equalsIgnoreCase("listbox")) {
      ListBox l = new ListBox();
      for(Map.Entry<String,String> v : data.getValues().entrySet()) {
        l.addItem(v.getKey(),v.getValue());
      }
      l.setEnabled(!data.isReadOnly());
      w = l;

    } else if(type.equalsIgnoreCase("file")) {
      TextBox t = new TextBox();
      t.getElement().setAttribute("type", "file");
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
      w = t;
    } else if(type.equalsIgnoreCase("date")) {
      TextBox t = new TextBox();
      t.getElement().setAttribute("type", "date");
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
      w = t;
    } else if(type.equalsIgnoreCase("user")) {
      //TODO : user selection
      TextBox t = new TextBox();
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
    } else if(type.equalsIgnoreCase("url")) {
      TextBox t = new TextBox();
      t.getElement().setAttribute("type", "url");
      t.setText(data.getValue());
      t.setReadOnly(data.isReadOnly());
    }
    container.add(w);
    if (data.isMandatory()) {
      //TODO
    }
  }

}
