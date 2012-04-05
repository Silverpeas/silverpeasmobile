package com.silverpeas.mobile.server.services;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.silverpeas.admin.ejb.AdminBm;
import com.silverpeas.admin.ejb.AdminBmHome;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import com.silverpeas.mobile.shared.dto.navigation.SpaceDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.NavigationException;
import com.silverpeas.mobile.shared.services.navigation.ServiceNavigation;
import com.stratelia.webactiv.beans.admin.ComponentInstLight;
import com.stratelia.webactiv.beans.admin.SpaceInstLight;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;

/**
 * Service de gestion de la navigation dans les espaces et apps.
 * @author svuillet
 */
public class ServiceNavigationImpl extends AbstractAuthenticateService implements ServiceNavigation {

	private static final long serialVersionUID = 1L;
	private AdminBm adminBm;
	
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
			e.printStackTrace();
		}
		return results;
	}

	private boolean containApp(String appType, SpaceInstLight space) throws RemoteException, Exception {
		List<String> appsIds = getAdminBm().getAvailCompoIds(space.getShortId(), getUserInSession().getId());
		for (String appId : appsIds) {
			ComponentInstLight app = getAdminBm().getComponentInstLight(appId);
			if (app.getName().equals(appType)) {
				return true;
			}							
		}
		return false;
	}
	
	private SpaceDTO populate(SpaceInstLight space) {
		SpaceDTO dto = new SpaceDTO();
		dto.setId(space.getShortId());
		dto.setLabel(space.getName());
		return dto;
	}
	
	private ApplicationInstanceDTO populate(ComponentInstLight app) {
		ApplicationInstanceDTO dto = new ApplicationInstanceDTO();
		dto.setId(app.getId());
		dto.setLabel(app.getLabel());
		return dto;
	}
	
	private AdminBm getAdminBm() throws Exception {
		if (adminBm == null) {			 
			AdminBmHome home = EJBUtilitaire.getEJBObjectRef(JNDINames.ADMINBM_EJBHOME, AdminBmHome.class);
			adminBm = home.create();
		}
		return adminBm;
	}
}
