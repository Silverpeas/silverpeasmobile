/*
 * Copyright (C) 2000 - 2019 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.media.MediaDTO;
import org.silverpeas.mobile.shared.dto.media.PhotoDTO;
import org.silverpeas.mobile.shared.dto.media.SoundDTO;
import org.silverpeas.mobile.shared.dto.media.VideoDTO;
import org.silverpeas.mobile.shared.dto.media.VideoStreamingDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.MediaException;

import java.util.List;

@RemoteServiceRelativePath("Media")
public interface ServiceMedia extends RemoteService {
  public void uploadPicture(String name, String data, String idGallery, String idAlbum) throws
                                                                                        MediaException, AuthenticationException;
  public List<ApplicationInstanceDTO> getAllGalleries() throws MediaException, AuthenticationException;

  public MediaDTO getMedia(String id) throws MediaException, AuthenticationException;

  public StreamingList<BaseDTO> getAlbumsAndPictures(String instanceId, String albumId, int callNumber) throws
                                                                               MediaException, AuthenticationException;
  public PhotoDTO getOriginalPicture(String instanceId, String pictureId) throws MediaException, AuthenticationException;
  public PhotoDTO getPreviewPicture(String instanceId, String pictureId) throws MediaException, AuthenticationException;

  public SoundDTO getSound(String instanceId, String soundId) throws MediaException, AuthenticationException;
  public VideoDTO getVideo(String instanceId, String videoId) throws MediaException, AuthenticationException;
  public VideoStreamingDTO getVideoStreaming(final String instanceId, final String videoId) throws MediaException, AuthenticationException;
}
