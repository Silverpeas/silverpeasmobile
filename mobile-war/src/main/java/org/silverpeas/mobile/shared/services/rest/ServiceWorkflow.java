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

package org.silverpeas.mobile.shared.services.rest;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowFormActionDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancePresentationFormDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancesDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/mobile/workflow")
public interface ServiceWorkflow extends RestService {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{appId}/userField/{actionName}/{fieldName}/{role}")
  void getUserField(@PathParam("appId") String instanceId,
      @PathParam("actionName") String actionName, @PathParam("fieldName") String fieldName,
      @PathParam("role") String role, MethodCallback<List<BaseDTO>> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{appId}/instances/{role}")
  void getInstances(@PathParam("appId") String instanceId, @PathParam("role") String userRole,
      MethodCallback<WorkflowInstancesDTO> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{appId}/presentationForm/{role}")
  void getPresentationForm(@PathParam("appId") String instanceId, @PathParam("role") String role,
      MethodCallback<WorkflowInstancePresentationFormDTO> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{appId}/actionForm/{role}/{action}")
  void getActionForm(@PathParam("appId") String instanceId, @PathParam("role") String role,
      @PathParam("action") String action, MethodCallback<WorkflowFormActionDTO> callback);

}
