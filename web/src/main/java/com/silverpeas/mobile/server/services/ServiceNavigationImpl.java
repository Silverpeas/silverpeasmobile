/**
 * Copyright (C) 2000 - 2012 Silverpeas
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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import com.silverpeas.mobile.shared.dto.navigation.SpaceDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.NavigationException;
import com.silverpeas.mobile.shared.services.navigation.ServiceNavigation;
import com.stratelia.webactiv.beans.admin.AdminReference;
import com.stratelia.webactiv.beans.admin.ComponentInstLight;
import com.stratelia.webactiv.beans.admin.SpaceInstLight;

/**
 * Service de gestion de la navigation dans les espaces et apps.
 * 
 * @author svuillet
 */
public class ServiceNavigationImpl extends AbstractAuthenticateService implements ServiceNavigation {

	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(ServiceNavigationImpl.class);

	@Override
	public List<SilverpeasObjectDTO> getSpacesAndApps(String rootSpaceId, String appType) throws NavigationException, AuthenticationException {
		checkUserInSession();
		ArrayList<SilverpeasObjectDTO> results = new ArrayList<SilverpeasObjectDTO>();
		try {
			if (rootSpaceId == null) {
				String[] spaceIds = AdminReference.getAdminService().getAllSpaceIds(getUserInSession().getId());
				for (String spaceId : spaceIds) {
					SpaceInstLight space = AdminReference.getAdminService().getSpaceInstLightById(spaceId);
					if (space.getFatherId().equals("0")) {
						if (containApp(appType, space)) {
							results.add(populate(space));
						}
					}
				}
				Collections.sort(results);
			} else {
				String[] spaceIds = AdminReference.getAdminService().getAllSubSpaceIds(rootSpaceId, getUserInSession().getId());
				for (String spaceId : spaceIds) {
					SpaceInstLight space = AdminReference.getAdminService().getSpaceInstLightById(spaceId);
					if (space.getFatherId().equals(rootSpaceId)) {
						if (containApp(appType, space)) {
							results.add(populate(space));
						}
					}
				}
				Collections.sort(results);
				ArrayList<SilverpeasObjectDTO> partialResults = new ArrayList<SilverpeasObjectDTO>();
				List<ComponentInstLight> apps = AdminReference.getAdminService().getAvailCompoInSpace(getUserInSession().getId(), rootSpaceId);
				for (ComponentInstLight app : apps) {
					if (app.getName().equals(appType) && app.getDomainFatherId().equals(rootSpaceId)) {
						partialResults.add(populate(app));
					}
				}
				Collections.sort(partialResults);
				results.addAll(partialResults);
			}

		} catch (Exception e) {
			LOGGER.error("getSpacesAndApps", e);
		    throw new NavigationException(e.getMessage());
		}
		return results;
	}

	private boolean containApp(String appType, SpaceInstLight space) throws RemoteException, Exception {
		List<ComponentInstLight> apps = AdminReference.getAdminService().getAvailCompoInSpace(getUserInSession().getId(), space.getShortId());		
		for (ComponentInstLight app : apps) {
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
}
