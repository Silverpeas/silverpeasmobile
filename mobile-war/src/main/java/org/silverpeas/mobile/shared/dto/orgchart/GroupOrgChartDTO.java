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

package org.silverpeas.mobile.shared.dto.orgchart;

import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.UserDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupOrgChartDTO implements Serializable {

  private String id;
  private static final long serialVersionUID = 5338415881024885835L;
  private String name;
  private List<UserDTO> users = new ArrayList<>();
  private List<GroupOrgChartDTO> subGroups = new ArrayList<>();
  private List<UserDTO> boss = new ArrayList<>();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

    public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void addUser(final UserDTO user) { users.add(user); }

  public void addBoss(final UserDTO user) { boss.add(user); }

  public List<UserDTO> getUsers() {
    return users;
  }

  public void addSubGroup(final GroupOrgChartDTO subGroup) {
    subGroups.add(subGroup);
  }

  public List<GroupOrgChartDTO> getSubGroups() {
    return subGroups;
  }

  public void setUsers(List<UserDTO> users) {
    this.users = users;
  }

  public void setSubGroups(List<GroupOrgChartDTO> subGroups) {
    this.subGroups = subGroups;
  }

  public List<UserDTO> getBoss() { return boss; }

  public void setBoss(List<UserDTO> boss) { this.boss = boss; }

  public static GroupOrgChartDTO fromJSON(JsPropertyMap<Object> json) {
    GroupOrgChartDTO dto = new GroupOrgChartDTO();
    dto.setId((String) json.get("id"));
    dto.setName((String) json.get("name"));

    Object usersObj = json.get("users");
    if (usersObj != null) {
      JsArray<Object> usersArray = (JsArray<Object>) usersObj;

      List<UserDTO> users = new ArrayList<>();
      for (int i = 0; i < usersArray.length; i++) {
        JsPropertyMap<Object> userJson =
                (JsPropertyMap<Object>) usersArray.getAt(i);

        users.add(UserDTO.fromJSON(userJson));
      }
      dto.setUsers(users);
    }

    Object subGroupsObj = json.get("subGroups");
    if (subGroupsObj != null) {
      JsArray<Object> subGroupsArray = (JsArray<Object>) subGroupsObj;

      List<GroupOrgChartDTO> subGroups = new ArrayList<>();
      for (int i = 0; i < subGroupsArray.length; i++) {
        JsPropertyMap<Object> groupJson =
                (JsPropertyMap<Object>) subGroupsArray.getAt(i);

        subGroups.add(GroupOrgChartDTO.fromJSON(groupJson));
      }
      dto.setSubGroups(subGroups);
    }

    // boss
    Object bossObj = json.get("boss");
    if (bossObj != null) {
      JsArray<Object> bossArray = (JsArray<Object>) bossObj;

      List<UserDTO> bosses = new ArrayList<>();
      for (int i = 0; i < bossArray.length; i++) {
        JsPropertyMap<Object> bossJson =
                (JsPropertyMap<Object>) bossArray.getAt(i);

        bosses.add(UserDTO.fromJSON(bossJson));
      }
      dto.setBoss(bosses);
    }

    return dto;
  }

  public JsPropertyMap<Object> toJSON() {
    JsPropertyMap<Object> json = JsPropertyMap.of();

    json.set("id", id);
    json.set("name", name);

    // users
    JsArray<Object> usersArray = new JsArray<>();
    for (UserDTO user : users) {
      usersArray.push(user.toJSON());
    }
    json.set("users", usersArray);

    // subGroups
    JsArray<Object> subGroupsArray = new JsArray<>();
    for (GroupOrgChartDTO subGroup : subGroups) {
      subGroupsArray.push(subGroup.toJSON());
    }
    json.set("subGroups", subGroupsArray);

    // boss
    JsArray<Object> bossArray = new JsArray<>();
    for (UserDTO user : boss) {
      bossArray.push(user.toJSON());
    }
    json.set("boss", bossArray);

    return json;
  }
}