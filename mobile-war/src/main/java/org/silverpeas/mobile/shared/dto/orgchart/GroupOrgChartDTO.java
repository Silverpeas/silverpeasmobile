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

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonTypeName("GroupOrgChartDTO")
public class GroupOrgChartDTO extends BaseDTO implements Serializable {

  private static final long serialVersionUID = 5338415881024885835L;
  private String name;
  private List<UserDTO> users = new ArrayList<>();
  private List<GroupOrgChartDTO> subGroups = new ArrayList<>();
  private List<UserDTO> boss = new ArrayList<>();

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
}