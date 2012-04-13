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

import java.util.Date;
import java.util.List;

import com.silverpeas.mobile.server.dao.StatusDao;
import com.silverpeas.mobile.shared.dto.StatusDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.RSEexception;
import com.silverpeas.mobile.shared.services.ServiceRSE;
import com.silverpeas.socialNetwork.status.Status;
import com.silverpeas.socialNetwork.status.StatusService;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.UserDetail;

/**
 * Service de gestion du réseau social.
 * @author ethomas
 */
public class ServiceRSEImpl extends AbstractAuthenticateService implements ServiceRSE {

  private static final long serialVersionUID = 1L;
  private StatusDao statusDao = new StatusDao();
  private StatusService statusService = new StatusService();

  public String updateStatus(String textStatus) throws RSEexception, AuthenticationException {
    checkUserInSession();
    UserDetail user = getUserInSession();
    Status status = new Status(Integer.parseInt(user.getId()), new Date(), textStatus);
    return statusService.changeStatusService(status);
  }

  public List<StatusDTO> getStatus(int step) throws RSEexception, AuthenticationException {
    checkUserInSession();
    UserDetail user = getUserInSession();
    try {
      return statusDao.getAllStatus(Integer.parseInt(user.getId()), step);
    } catch (Exception ex) {
      SilverTrace.error("com.silverpeas.mobile.server.services", "ServiceRSEImpl.getAllStatus", "",
          ex);
      throw new RSEexception(ex);
    }
  }
}
