/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.socialnetwork.status.Status;
import org.silverpeas.core.socialnetwork.status.StatusService;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.core.web.rs.RESTWebService;
import org.silverpeas.core.web.rs.UserPrivilegeValidation;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.mobile.shared.dto.StatusDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@WebService
@Authorized
@Path(ServiceRSE.PATH)
public class ServiceRSE extends RESTWebService {

  @Context
  HttpServletRequest request;

  static final String PATH = "rse";

  @POST
  @Produces(MediaType.TEXT_PLAIN)
  @Path("status/{textStatus}")
  public String updateStatus(@PathParam("textStatus") String textStatus) {
    Status status = new Status(Integer.parseInt(getUser().getId()), new Date(), textStatus);
    return getStatusService().changeStatus(status);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("status")
  public StatusDTO getStatus() {
    Status status = getStatusService().getLastStatus(Integer.parseInt(getUser().getId()));
    StatusDTO dto = new StatusDTO();
    dto.setId(status.getId());
    dto.setCreationDate(status.getCreationDate());
    dto.setUserId(status.getUserId());
    dto.setDescription(status.getDescription());
    return dto;
  }

  private StatusService getStatusService() {
    return ServiceProvider.getService(StatusService.class);
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
  }
}
