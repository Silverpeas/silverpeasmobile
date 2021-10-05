/*
 * Copyright (C) 2000 - 2021 Silverpeas
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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.workflow.pages.widgets.ActionButton;
import org.silverpeas.mobile.client.apps.workflow.pages.widgets.Field;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.workflow.FieldPresentationDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancePresentationFormDTO;

import java.util.Map;

public class WorkflowPresentationPage extends PageContent {

  private static WorkflowPresentationPageUiBinder
      uiBinder = GWT.create(WorkflowPresentationPageUiBinder.class);

  private WorkflowInstancePresentationFormDTO data;

  @UiField
  UnorderedList fields;
  @UiField
  ActionsMenu actionsMenu;
  @UiField HeadingElement header;

  public void setData(final WorkflowInstancePresentationFormDTO data) {

  }

  public void setData(final WorkflowInstancePresentationFormDTO data, final ApplicationInstanceDTO instance) {
    this.data = data;
    // display presentation form
    header.setInnerText(data.getTitle());
    for (FieldPresentationDTO field : data.getFields()) {
      field.setInstanceId(instance.getId());
      String value = field.getValue();
      if (field.getValue() != null && !value.isEmpty()) {
        Field f = new Field();
        f.setData(field);
        fields.add(f);
      }
    }
    for (Map.Entry<String, String> action : data.getActions().entrySet()) {
      ActionButton act = new ActionButton();
      act.setId(action.getKey());
      act.init(data.getInstanceId(), action.getKey(), action.getValue(), data.getState());
      actionsMenu.addAction(act);
    }
  }

  interface WorkflowPresentationPageUiBinder extends UiBinder<Widget, WorkflowPresentationPage> {
  }

  public WorkflowPresentationPage() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void stop() {
    super.stop();
  }


}