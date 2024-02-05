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
import org.fusesource.restygwt.client.TextCallback;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.HomePageDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import org.silverpeas.mobile.shared.dto.navigation.SpaceDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author svu
 */
@Path("/mobile/navigation")
public interface ServiceNavigation extends RestService {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("space/{spaceId}/")
  public void getSpace(@PathParam("spaceId") String spaceId, MethodCallback<SpaceDTO> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("personalSpace/{userId}/")
  public void getPersonnalSpaceContent(@PathParam("userId") String userId, MethodCallback<List<ApplicationInstanceDTO>> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("spacesAndApps/{rootSpaceId}/")
  public void getSpacesAndApps(@PathParam("rootSpaceId") String rootSpaceId,
      MethodCallback<List<SilverpeasObjectDTO>> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("app/{instanceId}/{contentId}/{contentType}/")
  public void getApp(@PathParam("instanceId") String instanceId,
      @PathParam("contentId") String contentId, @PathParam("contentType") String contentType, MethodCallback<ApplicationInstanceDTO> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("user/{login}/{domainId}/")
  public void getUser(@PathParam("login") String login, @PathParam("domainId") String domainId, MethodCallback<DetailUserDTO> callback);


  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("setTabletMode")
  public void setTabletMode(MethodCallback<Boolean> callback);

  @GET
  @Path("clearAppCache")
  public void clearAppCache(MethodCallback<Void> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("homepage/{spaceId}/")
  public void getHomePageData(@PathParam("spaceId") String spaceId, MethodCallback<HomePageDTO> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("isWorkflowApp/{instanceId}/")
  public void isWorkflowApp(@PathParam("instanceId") String intanceId, MethodCallback<Boolean> callback);

  @PUT
  @Path("storeTokenMessaging/{token}/")
  public void storeTokenMessaging(@PathParam("token") String token, MethodCallback<Void> callback);
}
