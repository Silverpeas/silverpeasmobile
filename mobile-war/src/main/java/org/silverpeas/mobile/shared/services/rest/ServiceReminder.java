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
import org.silverpeas.mobile.shared.dto.reminder.ReminderDTO;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * @author svu
 */
@Path("/reminder")
public interface ServiceReminder extends RestService {

  @GET
  @Path("/{componentInstanceId}/{type}/{localId}/possibledurations/{property}")
  public void getPossibleDurations(@PathParam("componentInstanceId") String componentInstanceId,
      @PathParam("type") String type, @PathParam("localId") String localId,
      @PathParam("property") String property, MethodCallback<List<String>> callback);

  @GET
  @Path("/{componentInstanceId}/{type}/{localId}")
  public void getReminders(@PathParam("componentInstanceId") String componentInstanceId,
      @PathParam("type") String type, @PathParam("localId") String localId, MethodCallback<List<ReminderDTO>> callback);



  @POST
  @Path("/{componentInstanceId}/{type}/{localId}")
  public void createReminder(@PathParam("componentInstanceId") String componentInstanceId,
      @PathParam("type") String type, @PathParam("localId") String localId, ReminderDTO dto, MethodCallback<ReminderDTO> callback);

  @DELETE
  @Path("/{componentInstanceId}/{type}/{localId}/{id}")
  public void deleteReminder(@PathParam("componentInstanceId") String componentInstanceId,
      @PathParam("type") String type, @PathParam("localId") String localId, @PathParam("id") String id, MethodCallback<Void> callback);


  @PUT
  @Path("/{componentInstanceId}/{type}/{localId}/{id}")
  public void updateReminder(@PathParam("componentInstanceId") String componentInstanceId,
      @PathParam("type") String type, @PathParam("localId") String localId, @PathParam("id") String id, ReminderDTO dto, MethodCallback<ReminderDTO> callback);

}
