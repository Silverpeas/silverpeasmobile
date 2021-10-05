/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.shared.dto.authentication;

import java.io.Serializable;

/**
 * @author svu
 */
public class UserProfileDTO implements Serializable {
  private String uri;
  private String contactsUri;
  private String webPage;
  private String avatar;
  private String domainName;
  private String fullName = "";
  private String language = "";
  private boolean connected = false;
  private boolean anonymous = false;
  private String apiToken;

  private String id;
  private String specificId;
  private String domainId;
  private String login;
  private String firstName;
  private String lastName;
  private String eMail;
  private String accessLevel;
  private String status;
  private boolean deletedState;
  private boolean deactivatedState;

  public String getUri() {
    return uri;
  }

  public void setUri(final String uri) {
    this.uri = uri;
  }

  public String getContactsUri() {
    return contactsUri;
  }

  public void setContactsUri(final String contactsUri) {
    this.contactsUri = contactsUri;
  }

  public String getWebPage() {
    return webPage;
  }

  public void setWebPage(final String webPage) {
    this.webPage = webPage;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(final String avatar) {
    this.avatar = avatar;
  }

  public String getDomainName() {
    return domainName;
  }

  public void setDomainName(final String domainName) {
    this.domainName = domainName;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(final String fullName) {
    this.fullName = fullName;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(final String language) {
    this.language = language;
  }

  public boolean isConnected() {
    return connected;
  }

  public void setConnected(final boolean connected) {
    this.connected = connected;
  }

  public boolean isAnonymous() {
    return anonymous;
  }

  public void setAnonymous(final boolean anonymous) {
    this.anonymous = anonymous;
  }

  public String getApiToken() {
    return apiToken;
  }

  public void setApiToken(final String apiToken) {
    this.apiToken = apiToken;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getSpecificId() {
    return specificId;
  }

  public void setSpecificId(final String specificId) {
    this.specificId = specificId;
  }

  public String getDomainId() {
    return domainId;
  }

  public void setDomainId(final String domainId) {
    this.domainId = domainId;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(final String login) {
    this.login = login;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  public String geteMail() {
    return eMail;
  }

  public void seteMail(final String eMail) {
    this.eMail = eMail;
  }

  public String getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(final String accessLevel) {
    this.accessLevel = accessLevel;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(final String status) {
    this.status = status;
  }

  public boolean isDeletedState() {
    return deletedState;
  }

  public void setDeletedState(final boolean deletedState) {
    this.deletedState = deletedState;
  }

  public boolean isDeactivatedState() {
    return deactivatedState;
  }

  public void setDeactivatedState(final boolean deactivatedState) {
    this.deactivatedState = deactivatedState;
  }
}
