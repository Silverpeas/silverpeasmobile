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

public class WorkflowInstancePresentationFormDTO {

  private String instanceId;
  private List<FieldPresentationDTO> fields;
  private Map<String, String> actions;
  private String title;
  private String state;
  private String id;

  public WorkflowInstancePresentationFormDTO() {
  }

    @Override
  public boolean equals(Object obj) {
    return ((WorkflowInstancePresentationFormDTO) obj).getId().equals(getId());
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public List<FieldPresentationDTO> getFields() {
    return fields;
  }

  public void setFields(final List<FieldPresentationDTO> fields) {
    this.fields = fields;
  }

  public Map<String, String> getActions() {
    return actions;
  }

  public void setActions(final Map<String, String> actions) {
    this.actions = actions;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(final String instanceId) {
    this.instanceId = instanceId;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public String getState() { return state; }

  public void setState(String state) { this.state = state; }

  public static WorkflowInstancePresentationFormDTO fromJSON(JsPropertyMap<Object> json) {
    WorkflowInstancePresentationFormDTO dto = new WorkflowInstancePresentationFormDTO();
    if (json == null) {
      return dto;
    }
    dto.setId(json.get("id") != null ? json.get("id").toString() : null);
    dto.setTitle(json.get("title") != null ? json.get("title").toString() : null);
    dto.setState(json.get("state") != null ? json.get("state").toString() : null);
    dto.setInstanceId(json.get("instanceId") != null ? json.get("instanceId").toString() : null);

    Object actionsObj = json.get("actions");
    if (actionsObj != null) {
      JsPropertyMap<String> actionsMap = (JsPropertyMap<String>) actionsObj;
      Map<String, String> actionss = new HashMap<>();
      String[] keys = getKeys(actionsMap);
      for (String key : keys) {
        actionss.put(key, actionsMap.get(key));
      }
      dto.setActions(actionss);
    }

    if (json.get("fields") != null) {
      JsArray<Object> list = (JsArray<Object>) json.get("fields");
      List<FieldPresentationDTO> result = new ArrayList<>();
      for (int i = 0; i < list.length; i++) {
        JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
        result.add(FieldPresentationDTO.fromJSON(map));
      }
      dto.setFields(result);
    } else {
      dto.setFields(new ArrayList<>());
    }

    return dto;
  }

  private static native String[] getKeys(JsPropertyMap<?> map) /*-{
    return Object.keys(map);
  }-*/;
}
