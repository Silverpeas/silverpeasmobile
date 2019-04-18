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

import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.socialnetwork.status.Status;
import org.silverpeas.core.socialnetwork.status.StatusService;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.mobile.server.common.SpMobileLogModule;
import org.silverpeas.mobile.shared.dto.StatusDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.RSEexception;
import org.silverpeas.mobile.shared.services.ServiceRSE;

import java.util.Date;

/**
 * Service de gestion du réseau social.
 * @author ethomas
 */
public class ServiceRSEImpl extends AbstractAuthenticateService implements ServiceRSE {

  private static final long serialVersionUID = 1L;

  @Override
  public String updateStatus(String textStatus) throws RSEexception, AuthenticationException {
    checkUserInSession();
    UserDetail user = getUserInSession();
    Status status = new Status(Integer.parseInt(user.getId()), new Date(), textStatus);
    return getStatusService().changeStatus(status);
  }

  @Override
  public StatusDTO getStatus() throws RSEexception, AuthenticationException {
    checkUserInSession();
    UserDetail user = getUserInSession();
    try {
      Status status = getStatusService().getLastStatus(Integer.parseInt(user.getId()));
      StatusDTO dto = new StatusDTO();
      dto.setId(status.getId());
      dto.setCreationDate(status.getCreationDate());
      dto.setUserId(status.getUserId());
      dto.setDescription(status.getDescription());
      return dto;
    } catch (Exception ex) {
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceRSEImpl.getStatus", ex);
      throw new RSEexception(ex);
    }
  }

  private StatusService getStatusService() {
    return ServiceProvider.getService(StatusService.class);
  }
}
