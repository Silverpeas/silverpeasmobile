package com.silverpeas.mobile.server.services;

import com.silverpeas.admin.ejb.AdminBusiness;
import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.shared.dto.RightDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import com.silverpeas.mobile.shared.dto.navigation.SpaceDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.NavigationException;
import com.silverpeas.mobile.shared.services.navigation.ServiceNavigation;
import com.stratelia.silverpeas.peasCore.MainSessionController;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.ComponentInstLight;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.SpaceInstLight;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service de gestion de la navigation dans les espaces et apps.
 * @author svuillet
 */
public class ServiceNavigationImpl extends AbstractAuthenticateService implements ServiceNavigation {

  private static final long serialVersionUID = 1L;
  private AdminBusiness adminBm;
  private OrganizationController organizationController = new OrganizationController();

  @Override
  public List<SilverpeasObjectDTO> getSpacesAndApps(String rootSpaceId, String appType) throws NavigationException, AuthenticationException {
    checkUserInSession();
    ArrayList<SilverpeasObjectDTO> results = new ArrayList<SilverpeasObjectDTO>();
    try {
      if (rootSpaceId == null) {
        List<String> spaceIds = getAdminBm().getAvailableSpaceIds(getUserInSession().getId());
        for (String spaceId : spaceIds) {
          SpaceInstLight space = getAdminBm().getSpaceInstLight(spaceId);
          if (space.getFatherId().equals("0")) {
            if (containApp(appType,space)) {
              results.add(populate(space));
            }
          }
        }
        Collections.sort(results);
      } else {
        List<String> spaceIds = getAdminBm().getAvailableSubSpaceIds(rootSpaceId, getUserInSession().getId());
        for (String spaceId : spaceIds) {
          SpaceInstLight space = getAdminBm().getSpaceInstLight(spaceId);
          if (space.getFatherId().equals(rootSpaceId)) {
            if (containApp(appType,space)) {
              results.add(populate(space));
            }
          }
        }
        Collections.sort(results);
        ArrayList<SilverpeasObjectDTO> partialResults = new ArrayList<SilverpeasObjectDTO>();
        List<String> appsIds = getAdminBm().getAvailCompoIds(rootSpaceId, getUserInSession().getId());
        for (String appId : appsIds) {
          ComponentInstLight app = getAdminBm().getComponentInstLight(appId);
          if (app.getName().equals(appType) && app.getDomainFatherId().equals("WA"+rootSpaceId)) {
            partialResults.add(populate(app));
          }
        }
        Collections.sort(partialResults);
        results.addAll(partialResults);
      }

    } catch (Exception e) {
      SilverTrace.error(SpMobileLogModule.getName(), "ServiceNavigationImpl.getSpacesAndApps", "root.EX_NO_MESSAGE", e);
    }
    return results;
  }

  @Override
  public ApplicationInstanceDTO getApp(String instanceId) throws NavigationException, AuthenticationException {
    ApplicationInstanceDTO dto = null;
    try {
      ComponentInstLight app = getAdminBm().getComponentInstLight(instanceId);
      dto = populate(app);
    } catch(Exception e) {
      SilverTrace.error(SpMobileLogModule.getName(), "ServiceNavigationImpl.getApp", "root.EX_NO_MESSAGE", e);
    }
    return dto;
  }

  private boolean containApp(String appType, SpaceInstLight space) throws Exception {
    List<String> appsIds = getAdminBm().getAvailCompoIds(space.getShortId(), getUserInSession().getId());
    for (String appId : appsIds) {
      ComponentInstLight app = getAdminBm().getComponentInstLight(appId);
      if (app.getName().equals(appType)) {
        return true;
      }
    }
    return false;
  }

  private String[] getUserRoles(String componentId, String userId) {
    return organizationController.getUserProfiles(userId, componentId);
  }

  private SpaceDTO populate(SpaceInstLight space) {
    SpaceDTO dto = new SpaceDTO();
    dto.setId(space.getShortId());
    dto.setLabel(space.getName());
    dto.setPersonal(space.isPersonalSpace());
    return dto;
  }

  private ApplicationInstanceDTO populate(ComponentInstLight app) {
    ApplicationInstanceDTO dto = new ApplicationInstanceDTO();
    dto.setId(app.getId());
    dto.setLabel(app.getLabel());
    dto.setType(app.getName());

    RightDTO rights = new RightDTO();
    String[] roles = getUserRoles(app.getId(), getUserInSession().getId());
    for (int i = 0; i < roles.length; i++) {
      if (roles[i].equals("admin")) {
        rights.setManager(true);
      }
      if (roles[i].equals("publisher")) {
        rights.setPublisher(true);
      }
      if (roles[i].equals("writer")) {
        rights.setWriter(true);
      }
      if (roles[i].equals("user")) {
        rights.setReader(true);
      }
    }
    dto.setRights(rights);

    try {
      String value = "";
      if (app.getName().equals("kmelia")) {
        value = getMainSessionController().getComponentParameterValue(app.getId(), "tabComments");
      } else if (app.getName().equals("gallery")) {
        value = getMainSessionController().getComponentParameterValue(app.getId(), "comments");
      }
      dto.setCommentable(value.equals("yes"));
    } catch (Exception e) {
      dto.setCommentable(false);
    }

    try {
      String value = "";
      if (app.getName().equals("kmelia")) {
        value = getMainSessionController().getComponentParameterValue(app.getId(), "tabContent");
      }
      dto.setAbleToStoreContent(value.equals("yes"));
    } catch (Exception e) {
      dto.setAbleToStoreContent(false);
    }

    return dto;
  }

  private AdminBusiness getAdminBm() throws Exception {
    if (adminBm == null) {
      adminBm = EJBUtilitaire.getEJBObjectRef(JNDINames.ADMINBM_EJBHOME, AdminBusiness.class);
    }
    return adminBm;
  }
}
