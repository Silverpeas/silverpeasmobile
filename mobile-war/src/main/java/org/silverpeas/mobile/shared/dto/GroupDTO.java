/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.shared.dto;

import jsinterop.base.JsPropertyMap;

public class GroupDTO extends BaseDTO {

  private String name;

  public GroupDTO() {
    setClassName(this.getClass().getSimpleName());
  }

  private int nbMembers;
  public int getNbMembers() {
    return nbMembers;
  }

  public void setNbMembers(int nbMembers) {
    this.nbMembers = nbMembers;
  }
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static GroupDTO fromJSON(JsPropertyMap<Object> json) {
    GroupDTO dto = new GroupDTO();

    dto.setId(json.get("id") != null ? json.get("id").toString() : null);
    dto.setName(json.get("name") != null ? json.get("name").toString() : null);

    Object nbMembersObj = json.get("nbMembers");
    if (nbMembersObj != null) {
      try {
        dto.setNbMembers(Integer.parseInt(nbMembersObj.toString()));
      } catch (NumberFormatException e) {
        dto.setNbMembers(0);
      }
    }

    return dto;
  }

  public JsPropertyMap<Object> toJSON() {
    JsPropertyMap<Object> json = JsPropertyMap.of();
    json.set("id", getId());
    json.set("name", getName());
    json.set("nbMembers", Integer.valueOf(getNbMembers()));
    json.set("className", getClass().getSimpleName());
    return json;
  }
}
