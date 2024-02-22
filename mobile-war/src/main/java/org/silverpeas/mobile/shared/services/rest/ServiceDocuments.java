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
import org.silverpeas.mobile.shared.dto.documents.AttachmentDTO;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.documents.TopicDTO;
import org.silverpeas.mobile.shared.dto.tickets.TicketDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/mobile/documents")
public interface ServiceDocuments extends RestService {
  @GET
  @Path("{appId}/topicsAndPublications/{rootTopicId}")
  public void getTopicsAndPublications(@PathParam("appId") String instanceId,
      @PathParam("rootTopicId") String rootTopicId, MethodCallback<List<BaseDTO>> callback);

  @GET
  @Path("{appId}/topics/{rootTopicId}")
  public void getTopics(@PathParam("appId") String instanceId,
      @PathParam("rootTopicId") String rootTopicId, MethodCallback<List<TopicDTO>> callback);

  @GET
  @Path("{appId}/publications/{topicId}")
  public void getPublications(@PathParam("appId") String instanceId,
      @PathParam("topicId") String topicId, MethodCallback<List<PublicationDTO>> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{appId}/publication/{id}")
  public void getPublication(@PathParam("appId") String instanceId, @PathParam("id") String id,
      @QueryParam("contributionId") String contributionId, @QueryParam("type") String type, MethodCallback<PublicationDTO> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{appId}/attachment/{attachmentId}")
  public void getAttachment(@PathParam("appId") String appId,
      @PathParam("attachmentId") String attachmentId, MethodCallback<AttachmentDTO> callback);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{appId}/tickets")
  public void getTickets(@PathParam("appId") String appId, List<TicketDTO> tickets,
                         MethodCallback<List<TicketDTO>> callback);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{appId}/deletetickets")
  public void deleteTickets(@PathParam("appId") String appId,
                            List<TicketDTO> tickets, MethodCallback<List<TicketDTO>> callback);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{appId}/nextpublication/{id}/{direction}")
  public void getNextPublication(@PathParam("appId") String appId,
                                 @PathParam("id") String id,
                                 @PathParam("direction") String direction, MethodCallback<PublicationDTO> callback);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{appId}/publish/{pubId}")
  public void publish(@PathParam("appId") String appId, @PathParam("pubId") String pubId, MethodCallback<PublicationDTO> callback);

}
