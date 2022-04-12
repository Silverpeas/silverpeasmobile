/*
 * Copyright (C) 2000 - 2022 Silverpeas
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
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.DomainDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/mobile/connection")
public interface ServiceConnection extends RestService {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("domains/")
  void getDomains(MethodCallback<List<DomainDTO>> callback);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Path("setTabletMode/")
  void setTabletMode(MethodCallback<Boolean> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("userExist/{login}/{domainId}")
  void userExist(@PathParam("login") String login, @PathParam("domainId") String domainId,
      MethodCallback<Boolean> callback);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Path("userAcceptsTermsOfService/")
  void userAcceptsTermsOfService(MethodCallback<Void> callback);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("changePwd/")
  void changePwd(String newPwd, MethodCallback<Void> callback);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("login")
  void login(List<String> ids, MethodCallback<DetailUserDTO> callback);

}
