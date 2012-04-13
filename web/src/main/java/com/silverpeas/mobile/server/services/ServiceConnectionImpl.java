/**
 * Copyright (C) 2000 - 2011 Silverpeas
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
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.server.services;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.silverpeas.admin.ejb.AdminBm;
import com.silverpeas.admin.ejb.AdminBmHome;
import com.silverpeas.mobile.shared.dto.DomainDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException.AuthenticationError;
import com.silverpeas.mobile.shared.services.ServiceConnection;
import com.stratelia.silverpeas.authentication.LoginPasswordAuthentication;
import com.stratelia.webactiv.beans.admin.Domain;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;

/**
 * Service de gestion des connexions.
 * @author svuillet
 */
public class ServiceConnectionImpl extends AbstractAuthenticateService implements ServiceConnection {

  private static final long serialVersionUID = 1L;

  private AdminBm adminBm;
  private OrganizationController organizationController = new OrganizationController();
  private LoginPasswordAuthentication lpAuth = new LoginPasswordAuthentication();

  public void login(String login, String password, String domainId)
      throws AuthenticationException {

    // vérification
    String code = lpAuth.authenticate(login, password, domainId, null);
    if (code.equals("Error_1")) {
      throw new AuthenticationException(AuthenticationError.BadCredential);
    } else if (code.equals("Error_2")) {
      throw new AuthenticationException(AuthenticationError.Host);
    } else if (code.equals("Error_5")) {
      throw new AuthenticationException(
          AuthenticationError.PwdNotAvailable);
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
  }

  public List<DomainDTO> getDomains() {
    Domain[] allDomains = organizationController.getAllDomains();
    ArrayList<DomainDTO> domains = new ArrayList<DomainDTO>();
    Mapper mapper = new DozerBeanMapper();
    for (int i = 0; i < allDomains.length; i++) {
      domains.add(mapper.map(allDomains[i], DomainDTO.class));
    }
    return domains;
  }

  private String getUserId(String login, String domainId) throws Exception {
    return getAdminBm().getUserIdByLoginAndDomain(login, domainId);
  }

  public UserDetail getUserDetail(String userId) {
    return organizationController.getUserDetail(userId);
  }

  private AdminBm getAdminBm() throws Exception {
    if (adminBm == null) {
      AdminBmHome home =
          EJBUtilitaire.getEJBObjectRef(JNDINames.ADMINBM_EJBHOME, AdminBmHome.class);
      adminBm = home.create();
    }
    return adminBm;
  }
}
