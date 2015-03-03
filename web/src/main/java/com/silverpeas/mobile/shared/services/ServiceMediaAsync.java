package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;
import com.silverpeas.mobile.shared.dto.media.SoundDTO;
import com.silverpeas.mobile.shared.dto.media.VideoDTO;
import com.silverpeas.mobile.shared.dto.media.VideoStreamingDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public interface ServiceMediaAsync {

	void uploadPicture(String name, String data, String idGallery, String idAlbum, AsyncCallback<Void> callback);

	void getAllGalleries(AsyncCallback<List<ApplicationInstanceDTO>> callback);

	void getOriginalPicture(String instanceId, String pictureId, AsyncCallback<PhotoDTO> callback);

	void getPreviewPicture(String instanceId, String pictureId, AsyncCallback<PhotoDTO> callback);

  void getAlbumsAndPictures(String instanceId, String albumId,
      final AsyncCallback<List<BaseDTO>> async);

  void getSound(String instanceId, String soundId, final AsyncCallback<SoundDTO> async);

  void getVideo(String instanceId, String videoId, final AsyncCallback<VideoDTO> async);

  void getVideoStreaming(final String instanceId, final String videoId,
      final AsyncCallback<VideoStreamingDTO> async);
}
