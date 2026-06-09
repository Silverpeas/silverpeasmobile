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

import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailUserDTO implements Serializable{

  private static final long serialVersionUID = 5388415881024885835L;
  private String id;
  private String lastName;
  private String eMail;
  private String firstName;
  private String avatar;
  private String phoneNumber;
  private String cellularPhoneNumber;
  private String faxPhoneNumber;
  private String status;
  private String language;
  private String token;
  private String zone;
  private String sessionKey;
  private boolean connected;
  private boolean notificationBox;
  private boolean ldap;
  private List<PropertyDTO> properties = new ArrayList<>();
  private Map<String, String> propertiesLabel = new HashMap<>();

    public static DetailUserDTO fromJSON(JsPropertyMap<Object> json) {
      DetailUserDTO dto = new DetailUserDTO();
      dto.setId((String) json.get("id"));
      dto.setLastName((String) json.get("lastName"));
      dto.seteMail((String) json.get("eMail"));
      dto.setFirstName((String) json.get("firstName"));

      dto.setAvatar((String) json.get("avatar"));
      dto.setPhoneNumber((String) json.get("phoneNumber"));
      dto.setCellularPhoneNumber((String) json.get("cellularPhoneNumber"));
      dto.setFaxPhoneNumber((String) json.get("faxPhoneNumber"));
      dto.setStatus((String) json.get("status"));
      dto.setLanguage((String) json.get("language"));
      dto.setToken((String) json.get("token"));
      dto.setZone((String) json.get("zone"));
      dto.setSessionKey((String) json.get("sessionKey"));

      Object connected = json.get("connected");
      if (connected != null) {
        dto.setConnected((boolean) connected);
      }

      Object notificationBox = json.get("notificationBox");
      if (notificationBox != null) {
        dto.setNotificationBox((boolean) notificationBox);
      }

      Object ldap = json.get("ldap");
      if (ldap != null) {
        dto.setLdap((boolean) ldap);
      }

      Object propertiesObj = json.get("properties");
      if (propertiesObj != null) {
        JsArray<Object> propertiesArray = (JsArray<Object>) propertiesObj;

        List<PropertyDTO> properties = new ArrayList<>();

        for (int i = 0; i < propertiesArray.length; i++) {
          JsPropertyMap<Object> propertyJson =
                  (JsPropertyMap<Object>) propertiesArray.getAt(i);

          properties.add(PropertyDTO.fromJSON(propertyJson));
        }

        dto.setProperties(properties);
      }



      Object propertiesLabelObj = json.get("propertiesLabel");
      if (propertiesLabelObj != null) {
        JsPropertyMap<Object> labelsJson =
                (JsPropertyMap<Object>) propertiesLabelObj;

        Map<String, String> labels = new HashMap<>();

        labelsJson.forEach(key -> {
          labels.put(key, (String) labelsJson.get(key));
        });

        dto.setPropertiesLabel(labels);
      }

      return dto;
    }

    public void addPropertyLabel(String prop, String label) {
    this.propertiesLabel.put(prop, label);
  }

  public void addProperty(String key, String value) {
    PropertyDTO dto = new PropertyDTO();
    dto.setKey(key);
    dto.setValue(value);
    this.properties.add(dto);
  }
  public void setProperties(List<PropertyDTO> properties) {
    this.properties = properties;
  }
  public void setPropertiesLabel(Map<String, String> propertiesLabel) {this.propertiesLabel = propertiesLabel;}
  public List<PropertyDTO> getProperties() {
    return properties;
  }
  public Map<String, String> getPropertiesLabel() {
    return propertiesLabel;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String LastName) {
    this.lastName = LastName;
  }

  public void seteMail(String EMail) {
    eMail = EMail;
  }

  public String geteMail() {
    return eMail;
  }

  public String getFirstName(){
    return firstName;
  }

  public void setFirstName(String firstName){
    this.firstName = firstName;
  }

  public String getAvatar(){
    return avatar;
  }

  public void setAvatar(String avatar){
    this.avatar = avatar;
  }

  public String getPhoneNumber(){
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber){
    this.phoneNumber = phoneNumber;
  }

  public String getCellularPhoneNumber() {
    return cellularPhoneNumber;
  }

  public void setCellularPhoneNumber(String cellularPhoneNumber) {
    this.cellularPhoneNumber = cellularPhoneNumber;
  }

  public String getFaxPhoneNumber() {
    return faxPhoneNumber;
  }

  public void setFaxPhoneNumber(String faxPhoneNumber) {
    this.faxPhoneNumber = faxPhoneNumber;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(final String status) {
    this.status = status;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getLanguage() {
    return language;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setConnected(final boolean connected) { this.connected = connected; }

  public boolean getConnected() { return connected; }

  public void setZone(final String zone) {
    this.zone = zone;
  }

  public String getZone() {
    return zone;
  }

  public void setSessionKey(final String sessionKey) {
    this.sessionKey = sessionKey;
  }

  public String getSessionKey() {
    return sessionKey;
  }

  public void setNotificationBox(final boolean notificationBox) {
    this.notificationBox = notificationBox;
  }

  public boolean isNotificationBox() {
    return notificationBox;
  }

  public void setLdap(boolean ldap) {
    this.ldap = ldap;
  }

  public boolean isLdap() {
    return ldap;
  }
}
