/*
 * Copyright (C) 2000 - 2026 Silverpeas
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

import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowDataDTO {

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

  public static WorkflowDataDTO fromJSON(JsPropertyMap<Object> json) {

    WorkflowDataDTO dto = new WorkflowDataDTO();
    if (json == null) {
      return dto;
    }
    dto.setId(json.get("id") != null ? json.get("id").toString() : null);

    // roles
    Object rolesObj = json.get("roles");
    if (rolesObj != null) {
      JsPropertyMap<String> rolesMap = (JsPropertyMap<String>) rolesObj;
      Map<String, String> roless = new HashMap<>();
      String[] keys = getKeys(rolesMap);
      for (String key : keys) {
        roless.put(key, rolesMap.get(key));
      }
      dto.setRoles(roless);
    }

    // rolesAllowedToCreate
    if (json.get("rolesAllowedToCreate") != null) {
      JsArray<Object> list = (JsArray<Object>) json.get("rolesAllowedToCreate");
      List<String> result = new ArrayList<>();
      for (int i = 0; i < list.length; i++) {
        result.add((String) list.getAt(i));
      }
      dto.setRolesAllowedToCreate(result);
    }

    // headerLabels
    Object headersLabelsObj = json.get("headerLabels");

    if (headersLabelsObj != null) {
      JsPropertyMap<JsArray<String>> headersLabelsMap = (JsPropertyMap<JsArray<String>>) headersLabelsObj;
      Map<String, List<String>> headersLab = new HashMap<>();
      String[] keys = getKeys(headersLabelsMap);
      for (String key : keys) {
        JsArray<String> array = headersLabelsMap.get(key);
        List<String> values = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
          values.add(array.getAt(i));
        }
        headersLab.put(key, values);
      }

      dto.setHeaderLabels(headersLab);
    }
    return dto;
  }

  private static native String[] getKeys(JsPropertyMap<?> map) /*-{
    return Object.keys(map);
  }-*/;
}
