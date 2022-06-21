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

package org.silverpeas.mobile.shared.dto;

import com.google.web.bindery.autobean.shared.AutoBean;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;

public class FullUserDTO extends DetailUserDTO {
  private String login;
  private String password;
  private String domainId;
  private String token;

  public FullUserDTO() {
    super();
  }

  public FullUserDTO(String login, String password, String domainId, DetailUserDTO user) {
    super();
    this.setLogin(login);
    this.setPassword(password);
    this.setDomainId(domainId);
    this.setToken(user.getToken());

    this.setId(user.getId());
    this.setAvatar(user.getAvatar());
    this.setCellularPhoneNumber(user.getCellularPhoneNumber());
    this.seteMail(user.geteMail());
    this.setFaxPhoneNumber(user.getFaxPhoneNumber());
    this.setFirstName(user.getFirstName());
    this.setLastName(user.getLastName());
    this.setPhoneNumber(user.getPhoneNumber());
    this.setStatus(user.getStatus());

    this.setLanguage(user.getLanguage());
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDomainId() {
    return domainId;
  }

  public void setDomainId(String domainId) {
    this.domainId = domainId;
  }

  @Override
  public String getToken() {
    return token;
  }

  @Override
  public void setToken(final String token) {
    this.token = token;
  }

  public AutoBean<IFullUser> getAutoBean () {
    AutoBean<IFullUser> b = LocalStorageHelper.factory.ifulluser();
    b.as().setConnected(getConnected());
    b.as().setAvatar(getAvatar());
    b.as().setId(getId());
    b.as().seteMail(geteMail());
    b.as().setDomainId(getDomainId());
    b.as().setFirstName(getFirstName());
    b.as().setLogin(getLogin());
    b.as().setToken(getToken());
    b.as().setCellularPhoneNumber(getCellularPhoneNumber());
    b.as().setNotificationBox(isNotificationBox());
    b.as().setSessionKey(getSessionKey());
    b.as().setZone(getZone());
    b.as().setLanguage(getLanguage());
    b.as().setStatus(getStatus());
    b.as().setFaxPhoneNumber(getFaxPhoneNumber());
    b.as().setPhoneNumber(getPhoneNumber());
    b.as().setLastName(getLastName());
    b.as().setPassword(getPassword());
    return b;
  }

  public static FullUserDTO getBean (AutoBean<IFullUser> b) {
    if (b == null) return null;
    FullUserDTO user = new FullUserDTO();
    user.setConnected(b.as().getConnected());
    user.setAvatar(b.as().getAvatar());
    user.setId(b.as().getId());
    user.seteMail(b.as().geteMail());
    user.setDomainId(b.as().getDomainId());
    user.setFirstName(b.as().getFirstName());
    user.setLogin(b.as().getLogin());
    user.setToken(b.as().getToken());
    user.setCellularPhoneNumber(b.as().getCellularPhoneNumber());
    user.setNotificationBox(b.as().isNotificationBox());
    user.setSessionKey(b.as().getSessionKey());
    user.setZone(b.as().getZone());
    user.setLanguage(b.as().getLanguage());
    user.setStatus(b.as().getStatus());
    user.setFaxPhoneNumber(b.as().getFaxPhoneNumber());
    user.setPhoneNumber(b.as().getPhoneNumber());
    user.setLastName(b.as().getLastName());
    user.setPassword(b.as().getPassword());
    return user;
  }
}