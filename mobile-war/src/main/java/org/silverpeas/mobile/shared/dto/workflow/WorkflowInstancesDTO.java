/*
 * Copyright (C) 2000 - 2021 Silverpeas
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
import java.util.Map;

public class WorkflowInstancesDTO extends BaseDTO implements Serializable {

  private static final long serialVersionUID = 2921606984249560882L;

  private List<String> headerLabels = new ArrayList<String>();
  private List<WorkflowInstanceDTO> instances;
  private Map<String,String> roles;
  private List<String> rolesAllowedToCreate;

  public WorkflowInstancesDTO() {
  }

  public List<WorkflowInstanceDTO> getInstances() {
    return instances;
  }

  public void setInstances(final List<WorkflowInstanceDTO> instances) {
    this.instances = instances;
  }

  public Map<String, String> getRoles() {
    return roles;
  }

  public void setRoles(final Map<String, String> roles) {
    this.roles = roles;
  }

  public List<String> getHeaderLabels() {
    return headerLabels;
  }

  public void setHeaderLabels(final List<String> headerLabels) {
    this.headerLabels = headerLabels;
  }

  public List<String> getRolesAllowedToCreate() {
    return rolesAllowedToCreate;
  }

  public void setRolesAllowedToCreate(final List<String> rolesAllowedToCreate) {
    this.rolesAllowedToCreate = rolesAllowedToCreate;
  }
}
