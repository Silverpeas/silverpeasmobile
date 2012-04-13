/**
 * Copyright (C) 2000 - 2011 Silverpeas
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
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.gallery.AlbumDTO;
import com.silverpeas.mobile.shared.dto.gallery.PhotoDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.GalleryException;

@RemoteServiceRelativePath("Gallery")
public interface ServiceGallery extends RemoteService {
  public void uploadPicture(String name, String data, String idGallery, String idAlbum)
      throws GalleryException, AuthenticationException;

  public List<ApplicationInstanceDTO> getAllGalleries() throws GalleryException,
      AuthenticationException;

  public List<AlbumDTO> getAllAlbums(String instanceId) throws GalleryException,
      AuthenticationException;

  public List<PhotoDTO> getAllPictures(String instanceId, String albumId) throws GalleryException,
      AuthenticationException;

  public PhotoDTO getOriginalPicture(String instanceId, String pictureId) throws GalleryException,
      AuthenticationException;

  public PhotoDTO getPreviewPicture(String instanceId, String pictureId) throws GalleryException,
      AuthenticationException;
}
