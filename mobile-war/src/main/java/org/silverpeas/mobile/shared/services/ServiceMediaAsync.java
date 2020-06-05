/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.media.MediaDTO;
import org.silverpeas.mobile.shared.dto.media.PhotoDTO;
import org.silverpeas.mobile.shared.dto.media.SoundDTO;
import org.silverpeas.mobile.shared.dto.media.VideoDTO;
import org.silverpeas.mobile.shared.dto.media.VideoStreamingDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

import java.util.List;

public interface ServiceMediaAsync {

	void uploadPicture(String name, String data, String idGallery, String idAlbum, AsyncCallback<Void> callback);

	void getAllGalleries(AsyncCallback<List<ApplicationInstanceDTO>> callback);

	void getPreviewPicture(String instanceId, String pictureId, AsyncCallback<PhotoDTO> callback);

  void getAlbumsAndPictures(String instanceId, String albumId, int callNumber,
      final AsyncCallback<StreamingList<BaseDTO>> async);

  void getSound(String instanceId, String soundId, final AsyncCallback<SoundDTO> async);

  void getVideo(String instanceId, String videoId, final AsyncCallback<VideoDTO> async);

  void getVideoStreaming(final String instanceId, final String videoId,
      final AsyncCallback<VideoStreamingDTO> async);

  void getMedia(String id, final AsyncCallback<MediaDTO> async);
}
