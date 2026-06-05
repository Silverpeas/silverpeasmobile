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

package org.silverpeas.mobile.shared.dto.authentication;

import com.google.web.bindery.autobean.shared.AutoBean;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;

import java.io.Serializable;

/**
 * @author svu
 */
public class UserProfileDTO implements IUserProfile, Serializable {
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
  private String emailAddress;
  private String accessLevel;

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

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(final String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(final String accessLevel) {
    this.accessLevel = accessLevel;
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
    b.as().setDomainId(getDomainId());
    b.as().setEmailAddress(getEmailAddress());
    b.as().setUri(getUri());
    b.as().setLastName(getLastName());
    b.as().setSpecificId(getSpecificId());
    b.as().setWebPage(getWebPage());
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
      user.setDomainId(b.as().getDomainId());
      user.setEmailAddress(b.as().getEmailAddress());
      user.setLanguage(b.as().getLanguage());
      user.setLogin(b.as().getLogin());
      user.setFirstName(b.as().getFirstName());
      user.setDomainName(b.as().getDomainName());
      user.setFullName(b.as().getFullName());
      user.setUri(b.as().getUri());
      user.setLastName(b.as().getLastName());
      user.setSpecificId(b.as().getSpecificId());
      user.setWebPage(b.as().getWebPage());
    }

    return user;
  }

  public static UserProfileDTO fromJSON(JsPropertyMap<Object> json) {
    UserProfileDTO dto = new UserProfileDTO();
    if (json == null) {
      return dto;
    }

    dto.setId((String) json.get("id"));
    dto.setSpecificId((String) json.get("specificId"));
    dto.setDomainId((String) json.get("domainId"));
    dto.setLogin((String) json.get("login"));
    dto.setFirstName((String) json.get("firstName"));
    dto.setLastName((String) json.get("lastName"));
    dto.setEmailAddress((String) json.get("emailAddress"));
    dto.setAccessLevel((String) json.get("accessLevel"));

    dto.setUri((String) json.get("uri"));
    dto.setContactsUri((String) json.get("contactsUri"));
    dto.setWebPage((String) json.get("webPage"));
    dto.setAvatar((String) json.get("avatar"));
    dto.setDomainName((String) json.get("domainName"));
    dto.setFullName((String) json.get("fullName"));
    dto.setLanguage((String) json.get("language"));
    dto.setApiToken((String) json.get("apiToken"));
    Object connected = json.get("connected");
    if (connected instanceof Boolean) {
      dto.setConnected((Boolean) connected);
    }

    Object anonymous = json.get("anonymous");
    if (anonymous instanceof Boolean) {
      dto.setAnonymous((Boolean) anonymous);
    }

    return dto;
  }

  public JsPropertyMap<Object> toJSON() {
    JsPropertyMap<Object> json = JsPropertyMap.of();

    json.set("id", id);
    json.set("specificId", specificId);
    json.set("domainId", domainId);

    json.set("login", login);
    json.set("firstName", firstName);
    json.set("lastName", lastName);
    json.set("emailAddress", emailAddress);
    json.set("accessLevel", accessLevel);

    json.set("uri", uri);
    json.set("contactsUri", contactsUri);
    json.set("webPage", webPage);

    json.set("avatar", avatar);
    json.set("domainName", domainName);
    json.set("fullName", fullName);
    json.set("language", language);
    json.set("apiToken", apiToken);

    json.set("connected", connected);
    json.set("anonymous", anonymous);

    return json;
  }

}
