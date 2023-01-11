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

package org.silverpeas.mobile.shared.dto.authentication;

import com.google.web.bindery.autobean.shared.AutoBean;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;

/**
 * @author svu
 */
public class UserProfileDTO implements IUserProfile {
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

  public AutoBean<IUserProfile> getAutoBean () {
    AutoBean<IUserProfile> b = LocalStorageHelper.factory.iuserprofile();
    b.as().setAnonymous(isAnonymous());
    b.as().setAccessLevel(getAccessLevel());
    b.as().setAvatar(getAvatar());
    b.as().setId(getId());
    b.as().setConnected(isConnected());
    b.as().setApiToken(getApiToken());
    b.as().setContactsUri(getContactsUri());
    b.as().setDeletedState(isDeletedState());
    b.as().setDomainId(getDomainId());
    b.as().seteMail(geteMail());
    b.as().setLanguage(getLanguage());
    b.as().setLogin(getLogin());
    b.as().setFirstName(getFirstName());
    b.as().setDomainName(getDomainName());
    b.as().setFullName(getFullName());
    b.as().setStatus(getStatus());
    b.as().setUri(getUri());
    b.as().setLastName(getLastName());
    b.as().setSpecificId(getSpecificId());
    b.as().setWebPage(getWebPage());
    b.as().setDeactivatedState(isDeactivatedState());
    return b;
  }

  public static UserProfileDTO getBean (AutoBean<IUserProfile> b) {
    UserProfileDTO user = null;
    if (b!= null) {
      user = new UserProfileDTO();
      user.setAnonymous(b.as().isAnonymous());
      user.setAccessLevel(b.as().getAccessLevel());
      user.setAvatar(b.as().getAvatar());
      user.setId(b.as().getId());
      user.setConnected(b.as().isConnected());
      user.setApiToken(b.as().getApiToken());
      user.setContactsUri(b.as().getContactsUri());
      user.setDeletedState(b.as().isDeletedState());
      user.setDomainId(b.as().getDomainId());
      user.seteMail(b.as().geteMail());
      user.setLanguage(b.as().getLanguage());
      user.setLogin(b.as().getLogin());
      user.setFirstName(b.as().getFirstName());
      user.setDomainName(b.as().getDomainName());
      user.setFullName(b.as().getFullName());
      user.setStatus(b.as().getStatus());
      user.setUri(b.as().getUri());
      user.setLastName(b.as().getLastName());
      user.setSpecificId(b.as().getSpecificId());
      user.setWebPage(b.as().getWebPage());
      user.setDeactivatedState(b.as().isDeactivatedState());
    }

    return user;
  }


}
