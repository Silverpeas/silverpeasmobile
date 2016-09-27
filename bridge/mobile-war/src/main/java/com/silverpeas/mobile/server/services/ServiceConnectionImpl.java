package com.silverpeas.mobile.server.services;

import com.silverpeas.mobile.server.dao.StatusDao;
import com.silverpeas.mobile.server.helpers.DataURLHelper;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.DomainDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException.AuthenticationError;
import com.silverpeas.mobile.shared.services.ServiceConnection;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.silverpeas.core.admin.domain.model.Domain;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.security.authentication.AuthenticationCredential;
import org.silverpeas.core.security.authentication.AuthenticationServiceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des connexions.
 * @author svuillet
 */
public class ServiceConnectionImpl extends AbstractAuthenticateService implements	ServiceConnection {

  private static final long serialVersionUID = 1L;

  private OrganizationController organizationController = OrganizationController.get();

  public void logout() throws AuthenticationException {
    getThreadLocalRequest().getSession().invalidate();
  }

  public DetailUserDTO login(String login, String password, String domainId)
      throws AuthenticationException {

    // vérification
    try {
      //String key = getAuthenticationBm().authenticate(login, password, domainId);

      AuthenticationCredential credential = AuthenticationCredential.newWithAsLogin(login)
          .withAsPassword(password).withAsDomainId(domainId);
      String key = AuthenticationServiceProvider.getService().authenticate(credential);
      if (key == null || key.startsWith("Error_")) {
        if (key.equals("Error_1")) {
          throw new AuthenticationException(AuthenticationError.BadCredential);
        } else if (key.equals("Error_2")) {
          throw new AuthenticationException(AuthenticationError.Host);
        } else if (key.equals("Error_5")) {
          throw new AuthenticationException(
              AuthenticationError.PwdNotAvailable);
        }
      }
    } catch (Exception e) {
      throw new AuthenticationException();
    }

    // récupération des informations de l'utilisateur
    String userId, authKey;
    try {
      userId = getUserId(login, domainId);
      authKey = AuthenticationServiceProvider.getService().getAuthenticationKey(login, domainId);
    } catch (Exception e) {
      throw new AuthenticationException(AuthenticationError.Host);
    }
    UserDetail user = getUserDetail(userId);
    setUserInSession(user);
    setUserkeyInSession(authKey);

    DetailUserDTO userDTO = new DetailUserDTO();
    Mapper mapper = new DozerBeanMapper();
    userDTO = mapper.map(user, DetailUserDTO.class);
    userDTO.setLanguage(user.getUserPreferences().getLanguage());

    String avatar = DataURLHelper.convertAvatarToUrlData(user.getAvatarFileName(), "40x");
    userDTO.setAvatar(avatar);

    try {
      userDTO.setStatus(new StatusDao().getStatus(Integer.parseInt(userId)).getDescription());
    } catch(Exception e) {
      userDTO.setStatus("");
    }

    return userDTO;
  }

  public List<DomainDTO> getDomains() {
    Domain[] allDomains = organizationController.getAllDomains();
    ArrayList<DomainDTO> domains = new ArrayList<>();
    Mapper mapper = new DozerBeanMapper();
    for (Domain allDomain : allDomains) {
      domains.add(mapper.map(allDomain, DomainDTO.class));
    }
    return domains;
  }

  private String getUserId(String login, String domainId) throws Exception {
    return Administration.get().getUserIdByLoginAndDomain(login, domainId);
  }

  public UserDetail getUserDetail(String userId) {
    return organizationController.getUserDetail(userId);
  }


}
