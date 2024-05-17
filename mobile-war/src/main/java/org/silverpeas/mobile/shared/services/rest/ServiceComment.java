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
import org.silverpeas.mobile.shared.dto.comments.CommentDTO;

import javax.ws.rs.*;
import java.util.List;

/**
 * @author svu
 */
@Path("/comments")
public interface ServiceComment extends RestService {


  @POST
  @Path("{componentId}/{contentType}/{contentId}/")
  public void saveNewComment(@PathParam("componentId") String componentId,
      @PathParam("contentType") String contentType, @PathParam("contentId") String contentId,
      CommentDTO commentToSave, MethodCallback<CommentDTO> callback);

  @GET
  @Path("{componentId}/{contentType}/{contentId}/")
  public void getAllComments(@PathParam("componentId") String componentId,
      @PathParam("contentType") String contentType, @PathParam("contentId") String contentId,
      MethodCallback<List<CommentDTO>> callback);

  @DELETE
  @Path("{componentId}/{contentType}/{contentId}/{commentId}")
  public void deleteComment(@PathParam("componentId") String componentId,
                            @PathParam("contentType") String contentType,
                            @PathParam("contentId") String contentId,
                            @PathParam("commentId") String commentId,
                            MethodCallback<Void> callback);
}
