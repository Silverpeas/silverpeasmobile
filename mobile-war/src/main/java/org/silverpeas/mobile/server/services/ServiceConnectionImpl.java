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

package org.silverpeas.mobile.server.services;

import org.silverpeas.core.SilverpeasException;
import org.silverpeas.core.admin.domain.model.Domain;
import org.silverpeas.core.admin.service.AdminException;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.security.authentication.AuthenticationCredential;
import org.silverpeas.core.security.authentication.AuthenticationServiceProvider;
import org.silverpeas.core.security.authentication.exception.AuthenticationUserMustAcceptTermsOfService;
import org.silverpeas.core.security.authentication.verifier.AuthenticationUserVerifierFactory;
import org.silverpeas.core.template.SilverpeasTemplate;
import org.silverpeas.core.template.SilverpeasTemplateFactory;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.logging.SilverLogger;
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
    if (key == null || key.startsWith("Error_")) {
      if (key.equals("Error_1")) {
        throw new AuthenticationException(AuthenticationError.BadCredential);
      } else if (key.equals("Error_2")) {
        throw new AuthenticationException(AuthenticationError.Host);
      } else if (key.equals("Error_5")) {
        throw new AuthenticationException(AuthenticationError.PwdNotAvailable);
      } else if (key.equals("Error_6")) {
        throw new AuthenticationException(AuthenticationError.LoginNotAvailable);
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
  public boolean showTermsOfService() throws AuthenticationException {
    SettingBundle resource = ResourceLocator.getSettingBundle("org.silverpeas.authentication.settings.authenticationSettings");
    String frequency = resource.getString("termsOfServiceAcceptanceFrequency");

    try {
      frequency = resource.getString("termsOfServiceAcceptanceFrequency.domain" + getUserInSession().getDomainId());
    } catch(Exception e) {
      SilverLogger.getLogger(this).debug("termsOfServiceAcceptanceFrequency.domain" + getUserInSession().getDomainId() + " not found");
    }

    if (frequency.equalsIgnoreCase("NEVER")) {
      return false;
    } else if (frequency.equalsIgnoreCase("ALWAYS")) {
      return true;
    } else {
      try {
        AuthenticationCredential credential = AuthenticationCredential.newWithAsLogin(getUserInSession().getLogin());
        credential.setDomainId(getUserInSession().getDomainId());
        AuthenticationUserVerifierFactory.getUserMustAcceptTermsOfServiceVerifier(credential).verify();
      } catch (AuthenticationUserMustAcceptTermsOfService authenticationUserMustAcceptTermsOfService) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getTermsOfServiceText() throws AuthenticationException {
      SettingBundle resource = ResourceLocator.getSettingBundle("org.silverpeas.authentication.settings.authenticationSettings");
      Boolean specificTemplate = false;
      try {
        specificTemplate = resource.getBoolean("termsOfServiceAcceptanceSpecificTemplateContent.domain" + getUserInSession().getDomainId());
      } catch(Exception e) {
        SilverLogger.getLogger(this).debug("termsOfServiceAcceptanceSpecificTemplateContent.domain" + getUserInSession().getDomainId() + " not found");
      }
      String content = "";
      SilverpeasTemplate template = SilverpeasTemplateFactory.createSilverpeasTemplateOnCore("termsOfService");
      if (specificTemplate) {
        content = template.applyFileTemplate(
            "termsOfService_domain" + getUserInSession().getDomainId() + "_" + getUserInSession().getUserPreferences().getLanguage());
      } else {
        content = template.applyFileTemplate(
            "termsOfService_" + getUserInSession().getUserPreferences().getLanguage());
      }
      return content;
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
