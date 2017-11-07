package com.silverpeas.mobile.server.services;

import com.silverpeas.mobile.server.dao.StatusDao;
import com.silverpeas.mobile.server.helpers.DataURLHelper;
import com.silverpeas.mobile.server.services.helpers.UserHelper;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.DomainDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException.AuthenticationError;
import com.silverpeas.mobile.shared.exceptions.NavigationException;
import com.silverpeas.mobile.shared.services.ServiceConnection;
import org.silverpeas.core.admin.domain.model.Domain;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.security.authentication.AuthenticationCredential;
import org.silverpeas.core.security.authentication.AuthenticationServiceProvider;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;

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

    DetailUserDTO userDTO = new DetailUserDTO();
    userDTO = UserHelper.getInstance().populate(user);

    String avatar = DataURLHelper.convertAvatarToUrlData(user.getAvatarFileName(), "40x");
    userDTO.setAvatar(avatar);

    try {
      userDTO.setStatus(new StatusDao().getStatus(Integer.parseInt(userId)).getDescription());
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
}
