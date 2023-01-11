/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.shared.dto.tickets;

import org.silverpeas.mobile.shared.dto.authentication.UserProfileDTO;

import java.io.Serializable;

/**
 * @author svu
 */
public class TicketDTO implements Serializable {
  private String sharedObjectId;
  private String sharedObjectType;
  private String componentId;
  private String validity;
  private String url;

  private String uri;
  private String endDateStr;
  private String endDateFormat;
  private String nbAccessMax;
  private String users;
  private String externalEmails;
  private String additionalMessage;

  private String creationDate;

  /****/

  private String nbAccess;
  private String token;
  private String valid;
  private String modified;
  private String continuous;

  private String name;

  public String getNbAccess() {
    return nbAccess;
  }

  public void setNbAccess(String nbAccess) {
    this.nbAccess = nbAccess;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getValid() {
    return valid;
  }

  public void setValid(String valid) {
    this.valid = valid;
  }

  public String getModified() {
    return modified;
  }

  public void setModified(String modified) {
    this.modified = modified;
  }

  public String getContinuous() {
    return continuous;
  }

  public void setContinuous(String continuous) {
    this.continuous = continuous;
  }

  public String getSharedObjectId() {
    return sharedObjectId;
  }

  public void setSharedObjectId(String sharedObjectId) {
    this.sharedObjectId = sharedObjectId;
  }

  public String getSharedObjectType() {
    return sharedObjectType;
  }

  public void setSharedObjectType(String sharedObjectType) {
    this.sharedObjectType = sharedObjectType;
  }

  public String getComponentId() {
    return componentId;
  }

  public void setComponentId(String componentId) {
    this.componentId = componentId;
  }

  public String getValidity() {
    return validity;
  }

  public void setValidity(String validity) {
    this.validity = validity;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getEndDateStr() {
    return endDateStr;
  }

  public void setEndDateStr(String endDateStr) {
    this.endDateStr = endDateStr;
  }

  public String getEndDateFormat() {
    return endDateFormat;
  }

  public void setEndDateFormat(String endDateFormat) {
    this.endDateFormat = endDateFormat;
  }

  public String getNbAccessMax() {
    return nbAccessMax;
  }

  public void setNbAccessMax(String nbAccessMax) {
    this.nbAccessMax = nbAccessMax;
  }

  public String getUsers() {
    return users;
  }

  public void setUsers(String users) {
    this.users = users;
  }

  public String getExternalEmails() {
    return externalEmails;
  }

  public void setExternalEmails(String externalEmails) {
    this.externalEmails = externalEmails;
  }

  public String getAdditionalMessage() {
    return additionalMessage;
  }

  public void setAdditionalMessage(String additionalMessage) {
    this.additionalMessage = additionalMessage;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
