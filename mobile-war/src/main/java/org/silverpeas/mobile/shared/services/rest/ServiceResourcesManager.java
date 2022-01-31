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
import org.fusesource.restygwt.client.TextCallback;
import org.silverpeas.mobile.shared.dto.reservations.ReservationDTO;
import org.silverpeas.mobile.shared.dto.reservations.ResourceDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author svu
 */
@Path("/mobile/resourcesManager")
public interface ServiceResourcesManager extends RestService {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("{appId}/resources/checkdates/{startDate}/{endDate}")
  public void checkDates(@PathParam("appId") String appId, @PathParam("startDate") String startDate,
      @PathParam("endDate") String endDate, TextCallback callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{appId}/resources/available/{startDate}/{endDate}")
  public void getAvailableResources(@PathParam("appId") String appId,
      @PathParam("startDate") String startDate, @PathParam("endDate") String endDate,
      MethodCallback<List<ResourceDTO>> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{appId}/reservations/my")
  public void getMyReservations(@PathParam("appId") String appId,
      MethodCallback<List<ReservationDTO>> callback);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{appId}/saveReservation")
  public void saveReservation(@PathParam("appId") String appId, ReservationDTO dto,
      MethodCallback<ReservationDTO> callback);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{appId}/reservation")
  public void deleteReservation(@PathParam("appId") String appId, ReservationDTO reservation,
      MethodCallback<Void> callback);
}
