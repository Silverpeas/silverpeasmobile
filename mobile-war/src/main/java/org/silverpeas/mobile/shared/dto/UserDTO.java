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

import com.fasterxml.jackson.annotation.JsonTypeName;
import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.orgchart.GroupOrgChartDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO extends BaseDTO implements Serializable {

  private static final long serialVersionUID = 5388415881024885835L;

  private String id;
  private String lastName;
  private String eMail;
  private String firstName;
  private String avatar;
  private String password;
  private List<PropertyDTO> properties = new ArrayList<PropertyDTO>();

    public static UserDTO fromJSON(JsPropertyMap<Object> json) {
      UserDTO dto = new UserDTO();
      dto.setId((String) json.get("id"));
      dto.setLastName((String) json.get("lastName"));
      dto.setFirstName((String) json.get("firstName"));
      dto.seteMail((String) json.get("eMail"));
      dto.setAvatar((String) json.get("avatar"));
      dto.setPassword((String) json.get("password"));

      // properties
      Object propertiesObj = json.get("properties");
      if (propertiesObj != null) {
        JsArray<Object> propertiesArray = (JsArray<Object>) propertiesObj;

        List<PropertyDTO> properties = new ArrayList<>();

        for (int i = 0; i < propertiesArray.length; i++) {
          JsPropertyMap<Object> propJson =
                  (JsPropertyMap<Object>) propertiesArray.getAt(i);

          properties.add(PropertyDTO.fromJSON(propJson));
        }

        dto.setProperties(properties);
      }

      return dto;
    }

    public List<PropertyDTO> getProperties() { return properties; }

  public void setProperties(List<PropertyDTO> properties) { this.properties = properties; }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String LastName) {
    this.lastName = LastName;
  }

  public String getFirstName(){
    return firstName;
  }

  public void setFirstName(String firstName){
    this.firstName = firstName;
  }

  public String geteMail() {
    return eMail;
  }

  public void seteMail(String eMail) {
    this.eMail = eMail;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public void addProperty(PropertyDTO property) { properties.add(property); }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    UserDTO other = (UserDTO) obj;
    if (getId() == null) {
      return false;
    } else if (!getId().equals(other.getId()))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    return Integer.parseInt(getId());
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public JsPropertyMap<Object> toJSON() {
    JsPropertyMap<Object> json = JsPropertyMap.of();
    json.set("id", id);
    json.set("lastName", lastName);
    json.set("firstName", firstName);
    json.set("eMail", eMail);
    json.set("avatar", avatar);
    json.set("password", password);

    // properties
    JsArray<Object> propertiesArray = new JsArray<>();
    for (PropertyDTO property : properties) {
      propertiesArray.push(property.toJSON());
    }
    json.set("properties", propertiesArray);

    return json;
  }
}
