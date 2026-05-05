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

package org.silverpeas.mobile.server.services;

import jakarta.activation.MimetypesFileTypeMap;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.silverpeas.components.gallery.GalleryComponentSettings;
import org.silverpeas.components.gallery.constant.MediaResolution;
import org.silverpeas.components.gallery.constant.MediaType;
import org.silverpeas.components.gallery.delegate.MediaDataCreateDelegate;
import org.silverpeas.components.gallery.model.*;
import org.silverpeas.components.gallery.service.GalleryService;
import org.silverpeas.core.ResourceReference;
import org.silverpeas.core.admin.component.model.ComponentInstLight;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.comment.service.CommentServiceProvider;
import org.silverpeas.core.node.model.NodePK;
import org.silverpeas.core.util.file.FileItem;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.core.webapi.media.streaming.StreamingProviderDataEntity;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.mobile.server.common.CommandCreateList;
import org.silverpeas.mobile.server.common.LocalDiskFileItem;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.comments.CommentDTO;
import org.silverpeas.mobile.shared.dto.media.*;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.MediaException;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.silverpeas.kernel.util.StringUtil.EMPTY;

/**
 * Service de gestion des galleries d'images.
 * @author svuillet
 */

@WebService
@Authorized
@Path(ServiceMedia.PATH + "/{appId}")
public class ServiceMedia extends AbstractRestWebService {

  @Inject
  private Administration admin;

  @Inject
  private GalleryService galleryService;

  @PathParam("appId")
  private String componentId;

  static final String PATH = "mobile/medialib";

  /**
   * Importation d'une image dans un album.
   */
  @POST
  @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
  @Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("add/{name}/{data}/{idGallery}/{idAlbum}")
  public void uploadPicture(@PathParam("name") String name, @PathParam("data") String data, @PathParam("idGallery") String idGallery, @PathParam("idAlbum") String idAlbum) {
    String extension = "jpg";
    if (data.contains("data:image/jpeg;base64,")) {
      extension = "jpg";
    }

    try {

      // stockage temporaire de la photo upload
      String tempDir = System.getProperty("java.io.tmpdir");
      String filename = tempDir + File.separator + name + "." + extension;
      OutputStream outputStream = new FileOutputStream(filename);

      outputStream.close();

      File file = new File(filename);
      // creation de la photo dans l'albums
      createPhoto(idGallery, idAlbum, file);
      Files.delete(file.toPath());

    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
  }

  private void createPhoto(String componentId, String albumId, File file)
      throws Exception {

    // création de la photo
    List<FileItem> parameters = new ArrayList<>();
    String type = new MimetypesFileTypeMap().getContentType(file);
    LocalDiskFileItem item = new LocalDiskFileItem(file, type);
    parameters.add(item);
    MediaDataCreateDelegate
        delegate = new MediaDataCreateDelegate(MediaType.Photo, "fr", albumId, parameters);

    Media newMedia = getGalleryService().createMedia(admin.getUserDetail(getUser().getId()), componentId, GalleryComponentSettings.getWatermark(componentId), delegate);

    newMedia.getId();
  }

  /**
   * Retourne la liste des albums d'une appli media.
   */
  private List<AlbumDTO> getAlbums(String instanceId, String rootAlbumId) throws MediaException {

    ArrayList<AlbumDTO> results = new ArrayList<>();
    try {
      if (rootAlbumId.equalsIgnoreCase("null")) rootAlbumId = null;
      if (rootAlbumId == null) {
        AlbumDTO rootAlbum = new AlbumDTO();
        ComponentInstLight app = admin.getComponentInstLight(instanceId);
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
      SilverLogger.getLogger(this).error(e);
      throw new MediaException(e);
    }
    return results;
  }

  private AlbumDTO populate(final AlbumDetail albumDetail) {
    AlbumDTO album = new AlbumDTO();
    album.setId(albumDetail.getId());
    album.setName(albumDetail.getName());
    int nbPhotos = countMedias(albumDetail);
    album.setCountMedia(nbPhotos);
    return album;
  }

  private int countMedias(final AlbumDetail albumDetail) {
    int count;
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

  private List<MediaDTO> getMedias(String instanceId, String albumId) throws MediaException {

    ArrayList<MediaDTO> results = new ArrayList<>();
    if (albumId.equalsIgnoreCase("null")) albumId = null;
    if (albumId == null) return results;
    try {
      Collection<Media> medias = getGalleryService().getAllMedia(new NodePK(albumId, instanceId),
              MediaCriteria.VISIBILITY.VISIBLE_ONLY);
      for (Media media : medias) {
        results.add(getMedia(media));
      }
      return results;

    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw new MediaException(e);
    }
  }

  @GET
  @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("media/{id}")
  public MediaDTO getMedia(@PathParam("id") String id) {
    MediaDTO dto;
    try {
      Media media = getGalleryService().getMedia(new MediaPK(id));
      dto = getMedia(media);
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw new WebApplicationException(e);
    }
    return dto;
  }

  private MediaDTO getMedia(Media media) {
    if (media.getType().isPhoto()) {
      return getPhoto(media.getId(), MediaResolution.SMALL);
    } else if (media.getType().isSound()) {
      return getSound(media);
    } else if (media.getType().isStreaming()) {
      return getVideoStreaming(media);
    } else if (media.getType().isVideo()) {
      return getVideo(media);
    }
    return null;
  }

  @GET
  @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("albumsandpics/{rootAlbumId}/{callNumber}")
  public StreamingList<BaseDTO> getAlbumsAndPictures(@PathParam("appId") String instanceId, @PathParam("rootAlbumId") String rootAlbumId, @PathParam("callNumber") int callNumber) {
    int callSize = 25;

    String cacheKey = instanceId+rootAlbumId;
    CommandCreateList command = () -> {
      List<BaseDTO> list = new ArrayList<>();
      list.addAll(getAlbums(instanceId, rootAlbumId));
      list.addAll(getMedias(instanceId, rootAlbumId));
      return list;
    };
    StreamingList<BaseDTO> streamingList;
    try {
      streamingList = createStreamingList(command, callNumber, callSize, cacheKey);
    } catch (AuthenticationException e) {
      throw new NotAuthorizedException(e);
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw new WebApplicationException(e);
    }
    return streamingList;
  }

  @GET
  @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("sound/{id}")
  public SoundDTO getSound(@PathParam("id") String soundId){

    Media sound;
    try {
      sound = getGalleryService().getMedia(new MediaPK(soundId));
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw e;
    }
    return getSound(sound);
  }

  @GET
  @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("video/{videoId}")
  public VideoDTO getVideo(@PathParam("videoId") String videoId) {
    Media video;
    try {
      video = getGalleryService().getMedia(new MediaPK(videoId));
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw e;
    }
    return getVideo(video);
  }

  @GET
  @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("videostream/{videoId}")
  public VideoStreamingDTO getVideoStreaming(@PathParam("videoId") String videoId) {
    Media video;
    try {
      video = getGalleryService().getMedia(new MediaPK(videoId));
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw e;
    }
    return getVideoStreaming(video);
  }

  /**
   * Retourne la photo preview.
   */
  @GET
  @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
  @Path("photo/{pictureId}")
  public PhotoDTO getPreviewPicture(@PathParam("pictureId") String pictureId) {
    PhotoDTO picture;
    try {
      picture = getPhoto(pictureId, MediaResolution.PREVIEW);
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw e;
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
    SimpleDateFormat durationFormat = getDurationFormat(d);

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

  private SoundDTO getSound(Media media) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    long d = media.getSound().getDuration() / 1000;
    Date duration = new Date(d*1000);
    SimpleDateFormat durationFormat = getDurationFormat(d);

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

  private SimpleDateFormat getDurationFormat(long d) {
    SimpleDateFormat durationFormat = new SimpleDateFormat("HH:mm:ss");
    if (d < 59) {
      durationFormat = new SimpleDateFormat("ss");
    } else if (d < (60 * 59)) {
      durationFormat = new SimpleDateFormat("mm:ss");
    }
    return durationFormat;
  }

  private PhotoDTO getPhoto(String pictureId, MediaResolution size) {
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

  private GalleryService getGalleryService() {
    return galleryService;
  }

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return this.componentId;
  }
}
