/*
 * Copyright (C) 2000 - 2025 Silverpeas
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
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.faq.QuestionDTO;
import org.silverpeas.mobile.shared.dto.media.MediaDTO;
import org.silverpeas.mobile.shared.dto.media.PhotoDTO;
import org.silverpeas.mobile.shared.dto.media.SoundDTO;
import org.silverpeas.mobile.shared.dto.media.VideoDTO;
import org.silverpeas.mobile.shared.dto.media.VideoStreamingDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * @author svu
 */
@Path("/mobile/medialib")
public interface ServiceMedia extends RestService {

  @GET
  @Path("add/{name}/{data}/{idGallery}/{idAlbum}")
  public void uploadPicture(@PathParam("name") String name, @PathParam("data") String data, @PathParam("idGallery") String idGallery, @PathParam("idAlbum") String idAlbum, MethodCallback<Void> callback);

  @GET
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("{appId}/media/{id}")
  public void getMedia(@PathParam("appId") String instanceId, @PathParam("id") String id, MethodCallback<MediaDTO> callback);

  @GET
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("{appId}/albumsandpics/{rootAlbumId}/{callNumber}")
  public void getAlbumsAndPictures(@PathParam("appId") String instanceId, @PathParam("rootAlbumId") String rootAlbumId, @PathParam("callNumber") int callNumber, MethodCallback<StreamingList<BaseDTO>> callback);

  @GET
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("{appId}/sound/{id}")
  public void getSound(@PathParam("appId") String instanceId, @PathParam("id") String soundId, MethodCallback<SoundDTO> callback);

  @GET
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("{appId}/video/{videoId}")
  public void getVideo(@PathParam("appId") String instanceId, @PathParam("videoId") String videoId, MethodCallback<VideoDTO> callback);


  @GET
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("{appId}/videostream/{videoId}")
  public void getVideoStreaming(@PathParam("appId") String instanceId, @PathParam("videoId") String videoId, MethodCallback<VideoStreamingDTO> callback);

  @GET
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("{appId}/photo/{pictureId}")
  public void getPreviewPicture(@PathParam("appId") String instanceId, @PathParam("pictureId") String pictureId, MethodCallback<PhotoDTO> callback);
}
