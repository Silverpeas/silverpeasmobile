/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.server.services;

import org.apache.commons.fileupload.FileItem;
import org.silverpeas.components.gallery.GalleryComponentSettings;
import org.silverpeas.components.gallery.constant.MediaResolution;
import org.silverpeas.components.gallery.constant.MediaType;
import org.silverpeas.components.gallery.delegate.MediaDataCreateDelegate;
import org.silverpeas.components.gallery.model.AlbumDetail;
import org.silverpeas.components.gallery.model.Media;
import org.silverpeas.components.gallery.model.MediaCriteria;
import org.silverpeas.components.gallery.model.MediaPK;
import org.silverpeas.components.gallery.model.Photo;
import org.silverpeas.components.gallery.service.GalleryService;
import org.silverpeas.components.gallery.service.MediaServiceProvider;
import org.silverpeas.core.ResourceReference;
import org.silverpeas.core.admin.component.model.ComponentInstLight;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.service.OrganizationControllerProvider;
import org.silverpeas.core.comment.service.CommentServiceProvider;
import org.silverpeas.core.node.model.NodePK;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.core.webapi.media.streaming.StreamingProviderDataEntity;
import org.silverpeas.mobile.server.common.CommandCreateList;
import org.silverpeas.mobile.server.common.LocalDiskFileItem;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.comments.CommentDTO;
import org.silverpeas.mobile.shared.dto.media.AlbumDTO;
import org.silverpeas.mobile.shared.dto.media.MediaDTO;
import org.silverpeas.mobile.shared.dto.media.PhotoDTO;
import org.silverpeas.mobile.shared.dto.media.SoundDTO;
import org.silverpeas.mobile.shared.dto.media.VideoDTO;
import org.silverpeas.mobile.shared.dto.media.VideoStreamingDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.MediaException;
import org.silverpeas.mobile.shared.services.ServiceMedia;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.silverpeas.core.util.StringUtil.EMPTY;

/**
 * Service de gestion des galleries d'images.
 * @author svuillet
 */
public class ServiceMediaImpl extends AbstractAuthenticateService implements ServiceMedia {

  private static final long serialVersionUID = 1L;
  private OrganizationController organizationController = OrganizationControllerProvider.getOrganisationController();

  /**
   * Importation d'une image dans un album.
   */
  public void uploadPicture(String name, String data, String idGallery, String idAlbum) throws
                                                                                        MediaException, AuthenticationException {
    checkUserInSession();

    String extension = "jpg";
    if (data.indexOf("data:image/jpeg;base64,") != -1) {
      data = data.substring("data:image/jpeg;base64,".length());
      extension = "jpg";
    }

    try {

      // stockage temporaire de la photo upload
      String tempDir = System.getProperty("java.io.tmpdir");
      String filename = tempDir + File.separator + name + "." + extension;
      OutputStream outputStream = new FileOutputStream(filename);

      outputStream.close();

      File file = new File(filename);

      // récupération de la configuration de la gallery
      boolean watermark = "yes".equalsIgnoreCase(organizationController.getComponentParameterValue(idGallery, "watermark"));
      boolean download = !"no".equalsIgnoreCase(organizationController.getComponentParameterValue(idGallery, "download"));
      String watermarkHD = organizationController.getComponentParameterValue(idGallery, "WatermarkHD");
      if(!StringUtil.isInteger(watermarkHD))  {
        watermarkHD = "";
      }
      String watermarkOther = organizationController.getComponentParameterValue(idGallery, "WatermarkOther");
      if(!StringUtil.isInteger(watermarkOther))  {
        watermarkOther = "";
      }

      // creation de la photo dans l'albums
      createPhoto(name, getUserInSession().getId(), idGallery, idAlbum, file, watermark, watermarkHD, watermarkOther, download);
      file.delete();

    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceMediaImpl.uploadPicture", "root.EX_NO_MESSAGE", e);
    }
  }

  private String createPhoto(String name, String userId, String componentId,
      String albumId, File file, boolean watermark, String watermarkHD,
      String watermarkOther, boolean download)
      throws Exception {

    // création de la photo
    List<FileItem> parameters = new ArrayList<FileItem>();
    String type = new MimetypesFileTypeMap().getContentType(file);
    LocalDiskFileItem item = new LocalDiskFileItem(file, type);
    parameters.add(item);
    MediaDataCreateDelegate
        delegate = new MediaDataCreateDelegate(MediaType.Photo, "fr", albumId, parameters);

    Media newMedia = getGalleryService().createMedia(getUserInSession(), componentId, GalleryComponentSettings.getWatermark(componentId), delegate);

    return newMedia.getId();
  }


  /**
   * Retourne la listes des galleries accessibles.
   */
  public List<ApplicationInstanceDTO> getAllGalleries() throws MediaException, AuthenticationException {
    checkUserInSession();

    ArrayList<ApplicationInstanceDTO> results = new ArrayList<ApplicationInstanceDTO>();
    try {
      String [] rootSpaceIds = Administration.get().getAllRootSpaceIds();
      for (String rootSpaceId : rootSpaceIds) {
        String [] componentIds = Administration.get().getAvailCompoIds(rootSpaceId);
        for (String componentId : componentIds) {
          ComponentInstLight instance = Administration.get().getComponentInstLight(componentId);
          if (instance.getName().equals("gallery")) {
            ApplicationInstanceDTO i = new ApplicationInstanceDTO();
            i.setId(instance.getId());
            i.setLabel(instance.getLabel());
            results.add(i);
          }
        }
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceMediaImpl.getAllGalleries", "root.EX_NO_MESSAGE", e);
    }

    Collections.sort(results);
    return results;
  }

  /**
   * Retourne la liste des albums d'une appli media.
   */
  private List<AlbumDTO> getAlbums(String instanceId, String rootAlbumId) throws MediaException, AuthenticationException {
    checkUserInSession();

    ArrayList<AlbumDTO> results = new ArrayList<AlbumDTO>();
    try {
      if (rootAlbumId == null) {
        AlbumDTO rootAlbum = new AlbumDTO();
        ComponentInstLight app = Administration.get().getComponentInstLight(instanceId);
        rootAlbum.setName(app.getLabel());
        rootAlbum.setRoot(true);
        results.add(rootAlbum);
        Collection<AlbumDetail> albums = getGalleryService().getAllAlbums(instanceId);
        for (AlbumDetail albumDetail : albums) {
          if (albumDetail.getLevel() == 2) {
            AlbumDTO album = populate(albumDetail);
            results.add(album);
          }
        }
      } else {
        AlbumDetail rootAlbum = getGalleryService().getAlbum(new NodePK(rootAlbumId, instanceId),
                MediaCriteria.VISIBILITY.VISIBLE_ONLY);
        AlbumDTO rootAlbumDTO = populate(rootAlbum);
        rootAlbumDTO.setRoot(true);
        results.add(rootAlbumDTO);

        Collection<AlbumDetail> albums = rootAlbum.getChildrenAlbumsDetails();
        for (AlbumDetail albumDetail : albums) {
          AlbumDTO album = populate(albumDetail);
          results.add(album);
        }
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceMediaImpl.getAlbums", "root.EX_NO_MESSAGE", e);
      throw new MediaException(e);
    }
    return results;
  }

  private AlbumDTO populate(final AlbumDetail albumDetail) throws Exception {
    AlbumDTO album = new AlbumDTO();
    album.setId(albumDetail.getId());
    album.setName(albumDetail.getName());
    int nbPhotos = countMedias(albumDetail);
    album.setCountMedia(nbPhotos);
    return album;
  }

  private int countMedias(final AlbumDetail albumDetail) throws Exception {
    int count = 0;
    Collection<Media> allMedias = getGalleryService().getAllMedia(albumDetail.getNodePK(),
            MediaCriteria.VISIBILITY.VISIBLE_ONLY);
    count = allMedias.size();
    // browser all sub albums for count all medias
    AlbumDetail thisAlbum = getGalleryService().getAlbum(albumDetail.getNodePK(),
            MediaCriteria.VISIBILITY.VISIBLE_ONLY);

    Collection<AlbumDetail> subAlbums = thisAlbum.getChildrenAlbumsDetails();
    for (AlbumDetail oneSubAlbum : subAlbums) {
      count = count + countMedias(oneSubAlbum);
    }
    return count;
  }

  private List<MediaDTO> getMedias(String instanceId, String albumId) throws MediaException, AuthenticationException {
    checkUserInSession();

    ArrayList<MediaDTO> results = new ArrayList<MediaDTO>();
    if (albumId == null) return results;
    try {
      Collection<Media> medias = getGalleryService().getAllMedia(new NodePK(albumId, instanceId),
              MediaCriteria.VISIBILITY.VISIBLE_ONLY);
      Iterator<Media> iMedias = medias.iterator();
      while (iMedias.hasNext()) {
        Media media = (Media) iMedias.next();
        results.add(getMedia(media));
      }
      return results;

    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceMediaImpl.getAllMedias", "root.EX_NO_MESSAGE", e);
      throw new MediaException(e);
    }
  }

  @Override
  public MediaDTO getMedia(String id) throws MediaException, AuthenticationException {
    MediaDTO dto = null;
    try {
      Media media = getGalleryService().getMedia(new MediaPK(id));
      dto = getMedia(media);
    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceMediaImpl.getMedia", "root.EX_NO_MESSAGE", e);
      throw new MediaException(e);
    }
    return dto;
  }

  private MediaDTO getMedia(Media media) throws Exception {
    if (media.getType().isPhoto()) {
      PhotoDTO photo = getPhoto(media.getInstanceId(), media.getId(), MediaResolution.SMALL);
      return photo;
    } else if (media.getType().isSound()) {
      SoundDTO sound = getSound(media);
      return sound;
    } else if (media.getType().isStreaming()) {
      VideoStreamingDTO video = getVideoStreaming(media);
      return video;
    } else if (media.getType().isVideo()) {
      VideoDTO video = getVideo(media);
      return video;
    }
    return null;
  }

  public StreamingList<BaseDTO> getAlbumsAndPictures(String instanceId, String rootAlbumId, int callNumber) throws
                                                                                   MediaException, AuthenticationException {
    checkUserInSession();
    int callSize = 25;
    String cacheKey = instanceId+rootAlbumId;
    CommandCreateList command = new CommandCreateList() {
      @Override
      public List execute() throws Exception {
        List list = new ArrayList<BaseDTO>();
        list.addAll(getAlbums(instanceId, rootAlbumId));
        list.addAll(getMedias(instanceId, rootAlbumId));
        return list;
      }
    };
    StreamingList streamingList = null;
    try {
      streamingList = createStreamingList(command, callNumber, callSize, cacheKey);
    } catch (AuthenticationException e) {
      throw e;
    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceMediaImpl.getAlbumsAndPictures", "root.EX_NO_MESSAGE", e);
      throw new MediaException(e);
    }
    return streamingList;
  }

  public SoundDTO getSound(String instanceId, String soundId) throws MediaException, AuthenticationException {
    checkUserInSession();
    Media sound = null;
    try {
      sound = getGalleryService().getMedia(new MediaPK(soundId));
    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceMediaImpl.getSound", "root.EX_NO_MESSAGE", e);
    }
    return getSound(sound);
  }

  @Override
  public VideoDTO getVideo(final String instanceId, final String videoId) throws MediaException, AuthenticationException {
    checkUserInSession();
    Media video = null;
    try {
      video = getGalleryService().getMedia(new MediaPK(videoId));
    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceMediaImpl.getVideo", "root.EX_NO_MESSAGE", e);
    }
    return getVideo(video);
  }

  @Override
  public VideoStreamingDTO getVideoStreaming(final String instanceId, final String videoId) throws MediaException, AuthenticationException {
    checkUserInSession();
    Media video = null;
    try {
      video = getGalleryService().getMedia(new MediaPK(videoId));
    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceMediaImpl.getVideoStreaming", "root.EX_NO_MESSAGE", e);
    }
    return getVideoStreaming(video);
  }

  /**
   * Retourne la photo preview.
   */
  public PhotoDTO getPreviewPicture(String instanceId, String pictureId) throws MediaException, AuthenticationException {
    checkUserInSession();

    PhotoDTO picture = null;
    try {
      picture = getPhoto(instanceId, pictureId, MediaResolution.PREVIEW);
    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceMediaImpl.getPreviewPicture", "root.EX_NO_MESSAGE", e);
    }
    return picture;
  }

  private VideoStreamingDTO getVideoStreaming(Media media) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    VideoStreamingDTO video = new VideoStreamingDTO();
    video.setName(media.getName());
    video.setTitle(media.getTitle());
    video.setId(media.getId());
    video.setMimeType(media.getType().getMediaWebUriPart());
    video.setInstance(media.getInstanceId());

    video.setCreator(media.getCreatorName());
    video.setCreationDate(sdf.format(media.getCreationDate()));

    if (media.getLastUpdater() != null) {
      video.setUpdater(media.getLastUpdaterName());
    } else {
      video.setUpdater(media.getCreatorName());
    }
    if (media.getLastUpdateDate() != null) {
      video.setUpdateDate(sdf.format(media.getLastUpdateDate()));
    } else {
      video.setUpdateDate(sdf.format(media.getCreationDate()));
    }

    final String urlVideo = media.getStreaming().getHomepageUrl();
    final Runnable noData = () -> {
      video.setUrl(EMPTY);
      video.setUrlPoster(EMPTY);
    };
    try {
      StreamingProviderDataEntity.from(urlVideo).ifPresentOrElse(e -> {
        video.setUrl(e.getSilverpeasEmbedUrl());
        video.setUrlPoster(e.getThumbnailUrl().toString());
        video.setDuration(e.getFormattedDurationHMS());
      }, noData);
    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceMediaImpl.getVideoStreaming", "root.EX_NO_MESSAGE", e);
      noData.run();
    }

    final ResourceReference ref = new ResourceReference(new MediaPK(video.getId()));
    video.setCommentsNumber(CommentServiceProvider.getCommentService().getCommentsCountOnResource(
          CommentDTO.TYPE_STREAMING, ref));

    return video;
  }

  private VideoDTO getVideo(Media media) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    long d = media.getVideo().getDuration() / 1000;
    Date duration = new Date(d*1000);
    SimpleDateFormat durationFormat = new SimpleDateFormat("HH:mm:ss");
    if (d < (60 * 59)) {
      durationFormat = new SimpleDateFormat("mm:ss");
    } else if (d < 59) {
      durationFormat = new SimpleDateFormat("ss");
    }

    VideoDTO video = new VideoDTO();
    video.setName(media.getName());
    video.setTitle(media.getTitle());
    video.setId(media.getId());
    video.setMimeType(media.getVideo().getFileMimeType().getMimeType());
    video.setInstance(media.getInstanceId());
    video.setDownload(media.getVideo().isDownloadAuthorized());
    video.setSize(media.getVideo().getFileSize());
    video.setDuration(durationFormat.format(duration));

    video.setCreator(media.getCreatorName());
    video.setCreationDate(sdf.format(media.getCreationDate()));

    if (media.getLastUpdater() != null) {
      video.setUpdater(media.getLastUpdaterName());
    } else {
      video.setUpdater(media.getCreatorName());
    }
    if (media.getLastUpdateDate() != null) {
      video.setUpdateDate(sdf.format(media.getLastUpdateDate()));
    } else {
      video.setUpdateDate(sdf.format(media.getCreationDate()));
    }
    final ResourceReference ref = new ResourceReference(new MediaPK(video.getId()));
    video.setCommentsNumber(CommentServiceProvider.getCommentService().getCommentsCountOnResource(CommentDTO.TYPE_VIDEO, ref));

    return video;
  }

  private byte[] getBytesFromInputStream(InputStream inStream)
      throws IOException {

    // Get the size of the file
    long streamLength = inStream.available();

    if (streamLength > Integer.MAX_VALUE) {
      // File is too large
    }

    // Create the byte array to hold the data
    byte[] bytes = new byte[(int) streamLength];

    // Read in the bytes
    int offset = 0;
    int numRead = 0;
    while (offset < bytes.length
        && (numRead = inStream.read(bytes,
        offset, bytes.length - offset)) >= 0) {
      offset += numRead;
    }

    // Ensure all the bytes have been read in
    if (offset < bytes.length) {
      throw new IOException("Could not completely read file ");
    }

    // Close the input stream and return bytes
    inStream.close();
    return bytes;
  }

  private SoundDTO getSound(Media media) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    long d = media.getSound().getDuration() / 1000;
    Date duration = new Date(d*1000);
    SimpleDateFormat durationFormat = new SimpleDateFormat("HH:mm:ss");
    if (d < (60 * 59)) {
      durationFormat = new SimpleDateFormat("mm:ss");
    } else if (d < 59) {
      durationFormat = new SimpleDateFormat("ss");
    }

    SoundDTO sound = new SoundDTO();
    sound.setName(media.getName());
    sound.setTitle(media.getTitle());
    sound.setId(media.getId());
    sound.setMimeType(media.getSound().getFileMimeType().getMimeType());
    sound.setInstance(media.getInstanceId());
    sound.setDownload(media.getSound().isDownloadAuthorized());
    sound.setSize(media.getSound().getFileSize());
    sound.setDuration(durationFormat.format(duration));

    sound.setCreator(media.getCreatorName());
    sound.setCreationDate(sdf.format(media.getCreationDate()));

    if (media.getLastUpdater() != null) {
      sound.setUpdater(media.getLastUpdaterName());
    } else {
      sound.setUpdater(media.getCreatorName());
    }
    if (media.getLastUpdateDate() != null) {
      sound.setUpdateDate(sdf.format(media.getLastUpdateDate()));
    } else {
      sound.setUpdateDate(sdf.format(media.getCreationDate()));
    }
    final ResourceReference ref = new ResourceReference(new MediaPK(sound.getId()));
    sound.setCommentsNumber(CommentServiceProvider.getCommentService().getCommentsCountOnResource(CommentDTO.TYPE_SOUND, ref));

    return sound;
  }

  private PhotoDTO getPhoto(String instanceId, String pictureId, MediaResolution size) throws Exception {
    PhotoDTO picture;
    Photo photoDetail = getGalleryService().getPhoto(new MediaPK(pictureId));
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    picture = new PhotoDTO();
    picture.setId(photoDetail.getId());
    picture.setDownload(photoDetail.isDownloadAuthorized());
    picture.setDataPhoto("/silverpeas/services/gallery/"+photoDetail.getInstanceId()+"/photos/"+photoDetail.getId()+"/content?resolution="+size.getName());
    picture.setFormat(size.name());
    picture.setTitle(photoDetail.getTitle());
    picture.setName(photoDetail.getName());
    picture.setSize(photoDetail.getFileSize());
    picture.setSizeH(photoDetail.getDefinition().getHeight());
    picture.setSizeL(photoDetail.getDefinition().getWidth());
    picture.setMimeType(photoDetail.getFileMimeType().getMimeType());
    picture.setInstance(photoDetail.getInstanceId());


    picture.setCreator(photoDetail.getCreatorName());
    picture.setCreationDate(sdf.format(photoDetail.getCreationDate()));

    if (photoDetail.getLastUpdater() != null) {
      picture.setUpdater(photoDetail.getLastUpdaterName());
    } else {
      picture.setUpdater(photoDetail.getCreatorName());
    }
    if (photoDetail.getLastUpdateDate() != null) {
      picture.setUpdateDate(sdf.format(photoDetail.getLastUpdateDate()));
    } else {
      picture.setUpdateDate(sdf.format(photoDetail.getCreationDate()));
    }


    final ResourceReference ref = new ResourceReference(
        new MediaPK(photoDetail.getId()));
    picture.setCommentsNumber(CommentServiceProvider.getCommentService().getCommentsCountOnResource(CommentDTO.TYPE_PHOTO, ref));

    return picture;
  }

  private GalleryService getGalleryService() throws Exception {
    return MediaServiceProvider.getMediaService();
  }
}
