/*
 * Copyright (C) 2000 - 2019 Silverpeas
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

package org.silverpeas.mobile.shared.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;

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
  private LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();

  public void addProperty(String key, String value) {
    properties.put(key, value);
  }

  public Set<String> getProperties() {
    return properties.keySet();
  }

  public String getPropertieValue(String key) {
    return properties.get(key);
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
}
