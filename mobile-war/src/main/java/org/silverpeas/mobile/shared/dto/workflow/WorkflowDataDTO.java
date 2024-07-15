/*
 * Copyright (C) 2000 - 2024 Silverpeas
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

package org.silverpeas.mobile.shared.dto.workflow;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class WorkflowDataDTO implements Serializable {



  private Map<String,List<String>> headerLabels;

  private Map<String,String> roles;
  private List<String> rolesAllowedToCreate;
  private String id;

  public WorkflowDataDTO() {
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public Map<String, String> getRoles() {
    return roles;
  }

  public void setRoles(final Map<String, String> roles) {
    this.roles = roles;
  }

  public Map<String,List<String>> getHeaderLabels() {
    return headerLabels;
  }

  public void setHeaderLabels(final Map<String,List<String>> headerLabels) {
    this.headerLabels = headerLabels;
  }

  public List<String> getRolesAllowedToCreate() {
    return rolesAllowedToCreate;
  }

  public void setRolesAllowedToCreate(final List<String> rolesAllowedToCreate) {
    this.rolesAllowedToCreate = rolesAllowedToCreate;
  }
}
