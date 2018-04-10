/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

package org.silverpeas.mobile.shared.dto.workflow;

import org.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkflowFormActionDTO extends BaseDTO implements Serializable {

  private static final long serialVersionUID = 2921606984249560882L;
  private List<WorkflowFieldDTO> fields = new ArrayList<WorkflowFieldDTO>();
  private String title;
  private String actionName;

  public WorkflowFormActionDTO() {
  }

  @Override
  public boolean equals(Object obj) {
    return ((WorkflowFormActionDTO) obj).getId().equals(getId());
  }

  public List<WorkflowFieldDTO> getFields() {
    return fields;
  }

  public void addField(WorkflowFieldDTO f) {
    fields.add(f);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getActionName() {
    return actionName;
  }

  public void setActionName(final String actionName) {
    this.actionName = actionName;
  }
}
