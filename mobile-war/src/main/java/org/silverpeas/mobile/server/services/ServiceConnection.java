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

package org.silverpeas.mobile.server.services;

import org.silverpeas.core.SilverpeasException;
import org.silverpeas.core.admin.domain.model.Domain;
import org.silverpeas.core.admin.service.AdminException;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.admin.user.model.UserFull;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.security.authentication.AuthenticationCredential;
import org.silverpeas.core.security.authentication.AuthenticationResponse;
import org.silverpeas.core.security.authentication.AuthenticationServiceProvider;
import org.silverpeas.core.security.authentication.exception.AuthenticationException;
import org.silverpeas.core.web.chat.listeners.ChatUserAuthenticationListener;
import org.silverpeas.core.web.rs.UserPrivilegeValidation;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.server.services.helpers.UserHelper;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.DomainDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException.AuthenticationError;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des connexions.
 * @author svuillet
 */
@WebService
@Path(ServiceConnection.PATH)
public class ServiceConnection extends AbstractRestWebService {

  @Inject
  ChatUserAuthenticationListener chatUserAuthenticationListener;

  @Context
  HttpServletRequest request;

  @Inject
  private OrganizationController organizationController;

  static final String PATH = "mobile/connection";

  private boolean isUserGUIMobileForTablets() {
    return getSettings().getBoolean("guiMobileForTablets", true);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("login")
  public DetailUserDTO login(List<String> ids) {

    String login = ids.get(0);
    String password = ids.get(1);
    String domainId = ids.get(2);

    // vérification
    AuthenticationCredential credential = getCredentials(login, password, domainId);
    AuthenticationResponse result =
        AuthenticationServiceProvider.getService().authenticate(credential);
    if (result == null || result.getStatus().isInError()) {
      AuthenticationResponse.Status status =
          result == null ? AuthenticationResponse.Status.BAD_LOGIN_PASSWORD : result.getStatus();
      WebApplicationException e;
      switch (status) {
        case NO_PASSWORD:
          e = new WebApplicationException(AuthenticationError.PwdNotAvailable.name());
          break;
        case PASSWORD_EXPIRED:
          e = new WebApplicationException(AuthenticationError.PwdExpired.name());
          break;
        case PASSWORD_TO_CHANGE:
          e = new WebApplicationException(AuthenticationError.PwdMustBeChanged.name());
          break;
        case PASSWORD_EMAIL_TO_CHANGE_ON_FIRST_LOGIN:
          e = new WebApplicationException(AuthenticationError.PwdMustBeChangedOnFirstLogin.name());
          break;
        case USER_ACCOUNT_BLOCKED:
          e = new WebApplicationException(AuthenticationError.UserAccountBlocked.name());
          break;
        case USER_ACCOUNT_DEACTIVATED:
          e = new WebApplicationException(AuthenticationError.UserAccountDeactivated.name());
          break;
        default:
          e = new WebApplicationException(AuthenticationError.BadCredential.name());
          break;
      }
      throw e;
    }

    // récupération des informations de l'utilisateur
    String userId;
    try {
      userId = getUserId(login, domainId);
    } catch (Exception e) {
      throw new WebApplicationException(AuthenticationError.Host.name());
    }
    UserDetail user = getUserDetail(userId);
    setUserInSession(user);

    try {
      setMainsessioncontroller(login, password, domainId);
    } catch (SilverpeasException e) {
      throw new WebApplicationException(AuthenticationError.CanCreateMainSessionController.name());
    }

    DetailUserDTO userDTO = UserHelper.getInstance().populate(user);

    String avatar = DataURLHelper.convertAvatarToUrlData(user.getAvatarFileName(),
        getSettings().getString("big.avatar.size", "40x"));
    userDTO.setAvatar(avatar);
    try {
      userDTO.setStatus(new ServiceRSE().getStatus().getDescription());
    } catch (Exception e) {
      userDTO.setStatus("");
    }

    // chat init
    chatUserAuthenticationListener.firstHomepageAccessAfterAuthentication(request, user, "");

    return userDTO;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("userExist/{login}/{domainId}")
  public Boolean userExist(@PathParam("login") String login,
      @PathParam("domainId") String domainId) {
    try {
      String id = getUserId(login, domainId);
      return id != null;
    } catch (Exception e) {
      return false;
    }
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Path("setTabletMode")
  public Boolean setTabletMode() {
    if (!isUserGUIMobileForTablets()) {
      request.getSession().setAttribute("tablet", Boolean.TRUE);
      return true;
    }
    return false;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("domains")
  public List<DomainDTO> getDomains() {
    Domain[] allDomains = organizationController.getAllDomains();
    ArrayList<DomainDTO> domains = new ArrayList<>();
    for (Domain allDomain : allDomains) {
      domains.add(populate(allDomain));
    }
    return domains;
  }

  private String getUserId(String login, String domainId) throws AdminException {
    return Administration.get().getUserIdByLoginAndDomain(login, domainId);
  }

  private UserDetail getUserDetail(String userId) {
    return organizationController.getUserDetail(userId);
  }

  private DomainDTO populate(Domain domain) {
    DomainDTO dto = new DomainDTO();
    dto.setName(domain.getName());
    dto.setId(domain.getId());
    return dto;
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("changePwd/")
  public void changePwd(String newPwd) {
    if (getUserInSession() == null) {
      throw new NotAuthorizedException(getHttpServletResponse());
    }
    UserFull user;
    try {
      user = Administration.get().getUserFull(getUserInSession().getId());
      user.setPassword(newPwd);
      Administration.get().updateUserFull(user);
    } catch (AdminException e) {
      throw new WebApplicationException(e);
    }
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Path("userAcceptsTermsOfService")
  public void userAcceptsTermsOfService() {
    try {
      Administration.get().userAcceptsTermsOfService(getUserInSession().getId());
    } catch (AdminException e) {
      throw new WebApplicationException(e);
    }
  }

  protected void setUserInSession(UserDetail user) {
    request.getSession().setAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME, user);
  }

  protected UserDetail getUserInSession() {
    return (UserDetail) request.getSession()
        .getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME);
  }

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return null;
  }

  @Override
  public void validateUserAuthorization(final UserPrivilegeValidation validation) {
    // no need to validate the authorization
  }

  private AuthenticationCredential getCredentials(String login, String password, String domainId) {
    try {
      return AuthenticationCredential.newWithAsLogin(login)
          .withAsPassword(password)
          .withAsDomainId(domainId);
    } catch (AuthenticationException e) {
      throw new WebApplicationException(AuthenticationError.BadCredential.name());
    }
  }
}
