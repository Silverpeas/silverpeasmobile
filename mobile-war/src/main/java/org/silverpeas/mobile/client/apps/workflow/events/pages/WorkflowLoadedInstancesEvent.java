/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.client.apps.workflow.events.pages;


import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancesDTO;

public class WorkflowLoadedInstancesEvent extends AbstractWorkflowPagesEvent {

  private WorkflowInstancesDTO data;
  private String instanceId;

  public WorkflowLoadedInstancesEvent() {
    super();
  }

  @Override
  protected void dispatch(WorkflowPagesEventHandler handler) {
    handler.loadInstances(this);
  }

  public WorkflowInstancesDTO getData() {
    return data;
  }

  public void setData(final WorkflowInstancesDTO data) {
    this.data = data;
  }

  public void setInstanceId(final String instanceId) {
    this.instanceId = instanceId;
  }

  public String getInstanceId() {
    return instanceId;
  }
}
