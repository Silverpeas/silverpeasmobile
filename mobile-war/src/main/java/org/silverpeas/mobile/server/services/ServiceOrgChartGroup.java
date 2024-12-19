/*
 * Copyright (C) 2000 - 2024 Silverpeas
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

import org.silverpeas.core.admin.component.model.ComponentInst;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.user.model.Group;
import org.silverpeas.core.admin.user.model.GroupDetail;
import org.silverpeas.core.admin.user.model.User;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.web.rs.UserPrivilegeValidation;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.mobile.shared.dto.GroupDTO;
import org.silverpeas.mobile.shared.dto.PropertyDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;
import org.silverpeas.mobile.shared.dto.orgchart.GroupOrgChartDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Service de gestion des Organigrammes groups.
 *
 * @author svuillet
 */
@WebService
@Authorized
@Path(ServiceOrgChartGroup.PATH + "/{appId}")
public class ServiceOrgChartGroup extends AbstractRestWebService {

  static final String PATH = "mobile/orgchartgroup";

  @Context
  HttpServletRequest request;

  @PathParam("appId")
  private String componentId;

  @GET
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("")
  public GroupOrgChartDTO getOrgChart() throws Exception {
    ComponentInst app = Administration.get().getComponentInst(componentId);
    String groupId = app.getParameterValue("ldapRoot");
    String titleField = app.getParameterValue("ldapAttTitle");
    String unitsChartCentralLabel = app.getParameterValue("unitsChartCentralLabel");
    GroupDetail root =  Administration.get().getGroup(groupId);
    GroupOrgChartDTO dto = populateOrga(root, titleField, unitsChartCentralLabel);
    return dto;
  }

  private GroupOrgChartDTO populateOrga(Group group, String titleField, String unitsChartCentralLabel) throws Exception {
    GroupOrgChartDTO dto = new GroupOrgChartDTO();
    dto.setId(group.getId());
    dto.setName(group.getName());
    dto.setName(group.getName());
    dto.setId(group.getId());
    String [] ids = Administration.get().getGroup(group.getId()).getUserIds();
    for (String id : ids) {
      User u = Administration.get().getUserDetail(id);
      dto.addUser(populate(u));
      String boss = isBoss(u, titleField, unitsChartCentralLabel);
      if (boss != null) {
        UserDTO b = populate(u);
        PropertyDTO prop = new PropertyDTO();
        prop.setKey("bossTitle");
        prop.setValue(boss);
        b.addProperty(prop);
        dto.addBoss(b);
      }
    }
    for (Group g : group.getSubGroups()) {
      dto.addSubGroup(populateOrga(g, titleField, unitsChartCentralLabel));
    }
    return dto;
  }

  private String isBoss(User u, String titleField, String unitsChartCentralLabel) throws Exception {
    String title = Administration.get().getUserFull(u.getId()).getValue(titleField);
    String [] rules = unitsChartCentralLabel.split(";");
    for (String rule : rules) {
      String [] r = rule.split("=");
      r[0] = r[0].trim();
      r[1] = r[1].trim();
      if (title.contains(r[1])) {
        return r[0];
      }
    }
    return null;
  }

  private UserDTO populate(User user) {
    UserDTO dto = new UserDTO();
    dto.setId(user.getId());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.seteMail(user.getEmailAddress());
    dto.setAvatar(user.getAvatar());
    return dto;
  }

  private GroupDTO populate(Group group) {
    GroupDTO dto = new GroupDTO();
    dto.setName(group.getName());
    dto.setId(group.getId());
    return dto;
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
