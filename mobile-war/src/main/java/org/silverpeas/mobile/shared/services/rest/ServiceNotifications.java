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
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationBoxDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationReceivedDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationSendedDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationToSendDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/mobile/notification")
public interface ServiceNotifications extends RestService {
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("allowedUsersAndGroups/{componentId}/{contentId}")
  void getAllowedUsersAndGroups(@PathParam("componentId") String componentId, @PathParam("contentId") String contentId, MethodCallback<List<BaseDTO>> callback);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("readed/{id}")
  void markAsReaden(@PathParam("id") long id, MethodCallback<Void> callback);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("readed/")
  void markAsRead(List<NotificationBoxDTO> selection, MethodCallback<Void> callback);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("")
  void delete(List<NotificationBoxDTO> selection, MethodCallback<Void> callback);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("send/")
  void send(NotificationToSendDTO notification, MethodCallback<Void> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("sended/")
  void getUserSendedNotifications(MethodCallback<List<NotificationSendedDTO>> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("received/")
  void getUserNotifications(MethodCallback<List<NotificationReceivedDTO>> callback);
}
