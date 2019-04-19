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

import org.silverpeas.components.formsonline.model.FormDetail;
import org.silverpeas.components.formsonline.model.FormsOnlineDatabaseException;
import org.silverpeas.components.formsonline.model.FormsOnlineService;
import org.silverpeas.components.formsonline.model.RequestsByStatus;
import org.silverpeas.core.annotation.RequestScoped;
import org.silverpeas.core.annotation.Service;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.core.webapi.base.RESTWebService;
import org.silverpeas.core.webapi.base.annotation.Authorized;
import org.silverpeas.mobile.shared.dto.formsonline.FormDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormRequestDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

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
@Service
@RequestScoped
@Path(ServiceFormsOnline.PATH)
@Authorized
public class ServiceFormsOnline extends RESTWebService {

  @Context
  HttpServletRequest request;

  static final String PATH = "formsOnline";


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(ServiceFormsOnline.PATH + "/sendables/{appId}")
  public List<FormDTO> getSendablesForms(@PathParam("appId") String appId) {
    List<FormDTO> dtos = new ArrayList<>();
    try {
      List<FormDetail> forms = FormsOnlineService.get().getAllForms(appId, getUser().getId(), true);
      for (FormDetail form : forms) {
        FormDTO dto = new FormDTO();
        if (form.isSendable() && form.isPublished()) {
          dto.setId(String.valueOf(form.getId()));
          dto.setTitle(form.getTitle());
          dto.setDescription(form.getDescription());
          dtos.add(dto);
        }
      }


    } catch (FormsOnlineDatabaseException e) {
      SilverLogger.getLogger(this).error(e);
    }
    return dtos;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(ServiceFormsOnline.PATH + "/{appId}/myrequests")
  public List<FormRequestDTO> getMyRequests(@PathParam("appId") String appId) {
    List<FormRequestDTO> requestDTOS = new ArrayList<>();

    try {
      RequestsByStatus reqs = FormsOnlineService.get().getAllUserRequests(appId, getUser().getId(), null);
      reqs.getAll();
      //TODO

    } catch (FormsOnlineDatabaseException e) {
      SilverLogger.getLogger(this).error(e);
    }

    return requestDTOS;
  }

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return null;
  }
}
