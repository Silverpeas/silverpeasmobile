/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.server.services;

import org.silverpeas.core.SilverpeasException;
import org.silverpeas.core.admin.domain.model.Domain;
import org.silverpeas.core.admin.service.AdminException;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.admin.user.model.UserFull;
import org.silverpeas.core.security.authentication.AuthenticationCredential;
import org.silverpeas.core.security.authentication.AuthenticationServiceProvider;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.server.services.helpers.UserHelper;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.DomainDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException.AuthenticationError;
import org.silverpeas.mobile.shared.exceptions.NavigationException;
import org.silverpeas.mobile.shared.services.ServiceConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des connexions.
 * @author svuillet
 */
public class ServiceConnectionImpl extends AbstractAuthenticateService
    implements ServiceConnection {

  private static final long serialVersionUID = 1L;

  private OrganizationController organizationController = OrganizationController.get();
  private static boolean useGUImobileForTablets;

  static {
    SettingBundle mobileSettings =
        ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
    useGUImobileForTablets = mobileSettings.getBoolean("guiMobileForTablets", true);
  }

  public DetailUserDTO login(String login, String password, String domainId)
      throws AuthenticationException {

    // vérification
    AuthenticationCredential credential =
        AuthenticationCredential.newWithAsLogin(login).withAsPassword(password)
            .withAsDomainId(domainId);
    String key = AuthenticationServiceProvider.getService().authenticate(credential);
    //SilverLogger.getLogger(this).debug("mobile authentification : {0} {1}", login, key);
    if (key == null || key.startsWith("Error_")) {
      if (key.equals("Error_1")) {
        throw new AuthenticationException(AuthenticationError.BadCredential);
      } else if (key.equals("Error_2")) {
        throw new AuthenticationException(AuthenticationError.Host);
      } else if (key.equals("Error_5")) {
        throw new AuthenticationException(AuthenticationError.PwdNotAvailable);
      } else if (key.equals("Error_6")) {
        throw new AuthenticationException(AuthenticationError.LoginNotAvailable);
      } else if (key.equals("Error_PwdExpired")) {
        throw new AuthenticationException(AuthenticationError.PwdExpired);
      } else if(key.equals("Error_PwdMustBeChanged")) {
        throw new AuthenticationException(AuthenticationError.PwdMustBeChanged);
      }

      throw new AuthenticationException();
    }

    // récupération des informations de l'utilisateur
    String userId;
    try {
      userId = getUserId(login, domainId);
    } catch (Exception e) {
      throw new AuthenticationException(AuthenticationError.Host);
    }
    UserDetail user = getUserDetail(userId);
    setUserInSession(user);

    try {
      setMainsessioncontroller(login, password, domainId);
    } catch (SilverpeasException e) {
      throw new AuthenticationException(AuthenticationError.CanCreateMainSessionController);
    }

    DetailUserDTO userDTO = new DetailUserDTO();
    userDTO = UserHelper.getInstance().populate(user);

    String avatar = DataURLHelper.convertAvatarToUrlData(user.getAvatarFileName(), getSettings().getString("big.avatar.size", "40x"));
    userDTO.setAvatar(avatar);
    try {
      userDTO.setStatus(new ServiceRSEImpl().getStatus().getDescription());
    } catch (Exception e) {
      userDTO.setStatus("");
    }

    return userDTO;
  }

  @Override
  public boolean userExist(final String login, final String domainId)
      throws AuthenticationException {
    try {
      String id = getUserId(login, domainId);
      return !(id == null);
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean setTabletMode() throws NavigationException, AuthenticationException {
    if (!useGUImobileForTablets) {
      getThreadLocalRequest().getSession().setAttribute("tablet", new Boolean(true));
      return true;
    }
    return false;
  }

  public List<DomainDTO> getDomains() {
    Domain[] allDomains = organizationController.getAllDomains();
    ArrayList<DomainDTO> domains = new ArrayList<>();
    for (Domain allDomain : allDomains) {
      domains.add(populate(allDomain));
    }
    return domains;
  }

  private String getUserId(String login, String domainId) throws Exception {
    return Administration.get().getUserIdByLoginAndDomain(login, domainId);
  }

  public UserDetail getUserDetail(String userId) {
    return organizationController.getUserDetail(userId);
  }

  private DomainDTO populate(Domain domain) {
    DomainDTO dto = new DomainDTO();
    dto.setName(domain.getName());
    dto.setId(domain.getId());
    return dto;
  }

  @Override
  public void changePwd(String newPwd) throws AuthenticationException {
    UserFull user = null;
    try {
      user = Administration.get().getUserFull(getUserInSession().getId());
      user.setPassword(newPwd);
      Administration.get().updateUserFull(user);
    } catch (AdminException e) {
      throw  new AuthenticationException(e);
    }
  }

  @Override
  public void userAcceptsTermsOfService() throws AuthenticationException {
    try {
      Administration.get().userAcceptsTermsOfService(getUserInSession().getId());
    } catch (AdminException e) {
      throw new AuthenticationException(e);
    }
  }
}
