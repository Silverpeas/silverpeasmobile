/*
 * Copyright (C) 2000 - 2026 Silverpeas
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

import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.media.*;

/**
 * Service to manage requests related to media library.
 * @author svu
 */
public class ServiceMedia extends AbstractService {

  private static final String PATH = "/silverpeas/services/mobile/medialib";

  /**
   * Uploads a picture.
   * @param name The name of the picture.
   * @param data The data of the picture.
   * @param idGallery The ID of the gallery.
   * @param idAlbum The ID of the album.
   * @param callback The callback to handle the response (no data returned).
   */
  public void uploadPicture(String name, String data, String idGallery, String idAlbum, RestCallback<Void> callback) {
    String url = PATH + "/add/" + encode(name) + "/" + encode(data) + "/" + encode(idGallery) + "/" + encode(idAlbum);
    get(url, result -> null, callback);
  }

  /**
   * Retrieves a media by its ID.
   * @param appId The ID of the application.
   * @param id The ID of the media.
   * @param callback The callback to handle the response (MediaDTO).
   */
  public void getMedia(String appId, String id, RestCallback<MediaDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/media/" + encode(id);
    get(url, result -> MediaDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves albums and pictures for a given root album.
   * @param appId The ID of the application.
   * @param rootAlbumId The ID of the root album.
   * @param callNumber The call number.
   * @param callback The callback to handle the response (StreamingList of BaseDTO).
   */
  public void getAlbumsAndPictures(String appId, String rootAlbumId, int callNumber, RestCallback<StreamingList<BaseDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/albumsandpics/" + encode(rootAlbumId) + "/" + callNumber;
    get(url, result -> StreamingList.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves a sound by its ID.
   * @param appId The ID of the application.
   * @param soundId The ID of the sound.
   * @param callback The callback to handle the response (SoundDTO).
   */
  public void getSound(String appId, String soundId, RestCallback<SoundDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/sound/" + encode(soundId);
    get(url, result -> SoundDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves a video by its ID.
   * @param appId The ID of the application.
   * @param videoId The ID of the video.
   * @param callback The callback to handle the response (VideoDTO).
   */
  public void getVideo(String appId, String videoId, RestCallback<VideoDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/video/" + encode(videoId);
    get(url, result -> VideoDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves a streaming video by its ID.
   * @param appId The ID of the application.
   * @param videoId The ID of the video.
   * @param callback The callback to handle the response (VideoStreamingDTO).
   */
  public void getVideoStreaming(String appId, String videoId, RestCallback<VideoStreamingDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/videostream/" + encode(videoId);
    get(url, result -> VideoStreamingDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves a preview picture by its ID.
   * @param appId The ID of the application.
   * @param pictureId The ID of the picture.
   * @param callback The callback to handle the response (PhotoDTO).
   */
  public void getPreviewPicture(String appId, String pictureId, RestCallback<PhotoDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/photo/" + encode(pictureId);
    get(url, result -> PhotoDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }
}