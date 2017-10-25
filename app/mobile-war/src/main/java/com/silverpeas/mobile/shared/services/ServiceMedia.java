package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.StreamingList;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;
import com.silverpeas.mobile.shared.dto.media.SoundDTO;
import com.silverpeas.mobile.shared.dto.media.VideoDTO;
import com.silverpeas.mobile.shared.dto.media.VideoStreamingDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.exceptions.MediaException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;

@RemoteServiceRelativePath("Media")
public interface ServiceMedia extends RemoteService {
  public void uploadPicture(String name, String data, String idGallery, String idAlbum) throws
          MediaException, AuthenticationException;
  public List<ApplicationInstanceDTO> getAllGalleries() throws MediaException, AuthenticationException;
  public StreamingList<BaseDTO> getAlbumsAndPictures(String instanceId, String albumId, int callNumber) throws
                                                                               MediaException, AuthenticationException;
  public PhotoDTO getOriginalPicture(String instanceId, String pictureId) throws MediaException, AuthenticationException;
  public PhotoDTO getPreviewPicture(String instanceId, String pictureId) throws MediaException, AuthenticationException;

  public SoundDTO getSound(String instanceId, String soundId) throws MediaException, AuthenticationException;
  public VideoDTO getVideo(String instanceId, String videoId) throws MediaException, AuthenticationException;
  public VideoStreamingDTO getVideoStreaming(final String instanceId, final String videoId) throws MediaException, AuthenticationException;
}
