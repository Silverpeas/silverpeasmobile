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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.exceptions.ContactException;
import com.silverpeas.mobile.shared.services.ServiceContact;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.UserDetail;

public class ServiceContactImpl extends AbstractAuthenticateService implements ServiceContact {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  private OrganizationController organizationController = new OrganizationController();
  private ArrayList<DetailUserDTO> listuserDTO;

  public List<DetailUserDTO> getAllContact() throws ContactException {
    listuserDTO = new ArrayList<DetailUserDTO>();
    List<UserDetail> userDetail = getAll();
    ArrayList<DetailUserDTO> users = new ArrayList<DetailUserDTO>();
    Mapper mapper = new DozerBeanMapper();
    Iterator<UserDetail> i = userDetail.iterator();
    while (i.hasNext()) {
      UserDetail us = i.next();
      users.add(mapper.map(us, DetailUserDTO.class));
    }
    listuserDTO = users;
    return listuserDTO;
  }

  public List<UserDetail> getAll() throws ContactException {
    UserDetail currentUser = getUserInSession();
    String userDomainId = currentUser.getDomainId();

    List<UserDetail> listUsers = Arrays.asList(organizationController.getAllUsers());
    List<UserDetail> listUsersOfSameDomain = getListUsersOfDomain(userDomainId, listUsers);

    return listUsersOfSameDomain;
  }

  private List<UserDetail> getListUsersOfDomain(String domainId, List<UserDetail> listUsers) {
    List<UserDetail> listUsersOfSameDomain = new ArrayList<UserDetail>();
    for (UserDetail userDetail : listUsers) {
      if (domainId.equals(userDetail.getDomainId())) {
        listUsersOfSameDomain.add(userDetail);
      }
    }
    return listUsersOfSameDomain;
  }
}
