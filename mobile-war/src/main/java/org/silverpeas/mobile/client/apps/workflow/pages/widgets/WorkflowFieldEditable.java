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

package org.silverpeas.mobile.client.apps.workflow.pages.widgets;

import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadUserFieldEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.components.forms.FieldEditable;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowFieldDTO;


public class WorkflowFieldEditable extends FieldEditable {

  @Override
  protected void sendEventToGetPossibleUsers() {
    WorkflowLoadUserFieldEvent event = new WorkflowLoadUserFieldEvent();
    event.setFieldName(getData().getName());
    event.setInstanceId(getData().getId());
    event.setActionName(((WorkflowFieldDTO)getData()).getActionName());
    EventBus.getInstance().fireEvent(event);
  }

  @Override
  public void setData(final FormFieldDTO data) {
    if (data instanceof WorkflowFieldDTO) {
      super.setData(data);
    } else {
      EventBus.getInstance().fireEvent(new ErrorEvent(new ClassCastException()));
    }
  }
}
