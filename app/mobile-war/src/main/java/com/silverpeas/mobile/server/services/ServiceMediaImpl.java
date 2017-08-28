package com.silverpeas.mobile.server.services;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverpeas.mobile.server.common.LocalDiskFileItem;
import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.helpers.RotationSupport;
import com.silverpeas.mobile.server.servlets.EasySSLProtocolSocketFactory;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;
import com.silverpeas.mobile.shared.dto.media.*;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.MediaException;
import com.silverpeas.mobile.shared.services.ServiceMedia;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.silverpeas.components.gallery.constant.MediaResolution;
import org.silverpeas.components.gallery.constant.MediaType;
import org.silverpeas.components.gallery.delegate.MediaDataCreateDelegate;
import org.silverpeas.components.gallery.model.*;
import org.silverpeas.components.gallery.service.GalleryService;
import org.silverpeas.components.gallery.service.MediaServiceProvider;
import org.silverpeas.core.admin.component.model.ComponentInstLight;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.service.OrganizationControllerProvider;
import org.silverpeas.core.comment.service.CommentServiceProvider;
import org.silverpeas.core.io.file.SilverpeasFile;
import org.silverpeas.core.node.model.NodePK;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.util.logging.SilverLogger;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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

    byte [] dataDecoded = org.apache.commons.codec.binary.Base64.decodeBase64(data.getBytes());

    try {
      // rotation de l'image si nécessaire
      Metadata metadata = ImageMetadataReader.readMetadata(new BufferedInputStream(new ByteArrayInputStream(dataDecoded)));
      Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
      InputStream in = new ByteArrayInputStream(dataDecoded);
      BufferedImage bi = ImageIO.read(in);
      if (directory != null) {
        int existingOrientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        bi = RotationSupport.adjustOrientation(bi, existingOrientation);
      }
      // stockage temporaire de la photo upload
      String tempDir = System.getProperty("java.io.tmpdir");
      String filename = tempDir + File.separator + name + "." + extension;
      OutputStream outputStream = new FileOutputStream(filename);

      ImageIO.write(bi, extension, outputStream);
      outputStream.close();

      File file = new File(filename);
      RotationSupport.setOrientation(RotationSupport.NOT_ROTATED, file);

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
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceMediaImpl.uploadPicture", "root.EX_NO_MESSAGE", e);
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

    Media newMedia = getGalleryService().createMedia(getUserInSession(), componentId, watermark, watermarkHD, watermarkOther, delegate);

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
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceMediaImpl.getAllGalleries", "root.EX_NO_MESSAGE", e);
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
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceMediaImpl.getAlbums", "root.EX_NO_MESSAGE", e);
      throw new MediaException(e);
    }
    return results;
  }

  private AlbumDTO populate(final AlbumDetail albumDetail) throws Exception {
    AlbumDTO album = new AlbumDTO();
    album.setId(String.valueOf(albumDetail.getId()));
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
        if (media.getType().isPhoto()) {
          PhotoDTO photo = getPhoto(media.getInstanceId(), media.getId(), MediaResolution.SMALL);
          results.add(photo);
        } else if (media.getType().isSound()) {
          SoundDTO sound = getSound(media);
          results.add(sound);
        } else if (media.getType().isStreaming()) {
          VideoStreamingDTO video = getVideoStreaming(media);
          results.add(video);
        } else if (media.getType().isVideo()) {
          VideoDTO video = getVideo(media);
          results.add(video);
        }
      }
      return results;

    } catch (Exception e) {
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceMediaImpl.getAllMedias", "root.EX_NO_MESSAGE", e);
      throw new MediaException(e);
    }
  }

  public List<BaseDTO> getAlbumsAndPictures(String instanceId, String rootAlbumId) throws
                                                                                   MediaException, AuthenticationException {
    checkUserInSession();
    ArrayList<BaseDTO> list = new ArrayList<BaseDTO>();
    list.addAll(getAlbums(instanceId, rootAlbumId));
    list.addAll(getMedias(instanceId, rootAlbumId));
    return list;
  }

  /**
   * Retourne la photo originale.
   */
  public PhotoDTO getOriginalPicture(String instanceId, String pictureId) throws MediaException, AuthenticationException {
    checkUserInSession();

    PhotoDTO picture = null;
    try {
      picture = getPhoto(instanceId, pictureId, MediaResolution.ORIGINAL);
      if (!picture.isDownload()) {
        picture = getPhoto(instanceId, pictureId, MediaResolution.LARGE);
      }

    } catch (Exception e) {
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceMediaImpl.getOriginalPicture", "root.EX_NO_MESSAGE", e);
    }
    return picture;
  }

  public SoundDTO getSound(String instanceId, String soundId) throws MediaException, AuthenticationException {
    checkUserInSession();
    Media sound = null;
    try {
      sound = getGalleryService().getMedia(new MediaPK(soundId));
    } catch (Exception e) {
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceMediaImpl.getSound", "root.EX_NO_MESSAGE", e);
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
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceMediaImpl.getVideo", "root.EX_NO_MESSAGE", e);
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
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceMediaImpl.getVideoStreaming", "root.EX_NO_MESSAGE", e);
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
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceMediaImpl.getPreviewPicture", "root.EX_NO_MESSAGE", e);
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

    String urlVideo = media.getStreaming().getHomepageUrl();
    if (urlVideo.contains("vimeo")) {
      String id = media.getStreaming().getProvider().extractStreamingId(urlVideo);
      video.setUrl("https://player.vimeo.com/video/" + id);
      String urlJson = "http://vimeo.com/api/v2/video/"+id+".json";
      HttpClient client = new HttpClient();
      HttpMethod method = new GetMethod(urlJson);
      try {
        client.executeMethod(method);
        String json = method.getResponseBodyAsString();
        json = json.substring(1, json.length()-1);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,String> map = objectMapper.readValue(json, HashMap.class);
        video.setUrlPoster(map.get("thumbnail_medium"));
      } catch (IOException e) {
        SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceMediaImpl.getVideoStreaming", "root.EX_NO_MESSAGE", e);
        video.setUrlPoster("");
      }
    } else if (urlVideo.contains("youtu")){
      String id = urlVideo.substring(urlVideo.lastIndexOf("/") + 1);
      int p = id.indexOf("?v=");
      if (p != -1) {
        id = id.substring(p+3);
      }
      video.setUrl("https://www.youtube.com/embed/" + id);
      video.setUrlPoster("http://img.youtube.com/vi/" + id + "/0.jpg");
    }

      video.setCommentsNumber(CommentServiceProvider.getCommentService().getCommentsCountOnPublication(CommentDTO.TYPE_STREAMING, new MediaPK(video.getId())));

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

    video.setDataPoster( getVideoPoster(media.getVideo()));

    video.setCommentsNumber(CommentServiceProvider.getCommentService().getCommentsCountOnPublication(CommentDTO.TYPE_VIDEO, new MediaPK(video.getId())));

    return video;
  }

  private String getVideoPoster(Video video) {
    long t = 0;
    String silverpeasServerUrl = getUserInSession().getDomain().getSilverpeasServerURL();
    if (!silverpeasServerUrl.contains("silverpeas")) {
      silverpeasServerUrl = silverpeasServerUrl + "/silverpeas";
    }

    String url = silverpeasServerUrl + "/services/gallery/" + video.getInstanceId() + "/videos/" + video.getId() + "/thumbnail/" + t;
    String data = "";
    InputStream input = null;
    String token = organizationController.getUserFull(getUserInSession().getId()).getToken();

    try {
      Protocol
          .registerProtocol("https", new Protocol("https", new EasySSLProtocolSocketFactory(), 443));

      HttpClient client = new HttpClient();
      HttpMethod method = new GetMethod(url);
      method.addRequestHeader("X-Silverpeas-Session", token);
      client.executeMethod(method);
      input = method.getResponseBodyAsStream();
      byte[] binaryData = getBytesFromInputStream(input);
      data = "data:" + method.getResponseHeader("Content-Type").getValue() + ";base64," + new String(Base64.encodeBase64(binaryData));
      method.releaseConnection();

    } catch (IOException e) {
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceMediaImpl.getVideoPoster", "root.EX_NO_MESSAGE", e);
    }
    return data;
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
    sound.setCommentsNumber(CommentServiceProvider.getCommentService().getCommentsCountOnPublication(CommentDTO.TYPE_SOUND, new MediaPK(sound.getId())));

    return sound;
  }

  private PhotoDTO getPhoto(String instanceId, String pictureId, MediaResolution size) throws Exception {
    PhotoDTO picture;
    Photo photoDetail = getGalleryService().getPhoto(new MediaPK(pictureId));
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    picture = new PhotoDTO();
    picture.setId(photoDetail.getId());
    picture.setDownload(photoDetail.isDownloadAuthorized());
    picture.setDataPhoto(getBase64ImageData(instanceId, photoDetail, size));
    picture.setFormat(size.name());
    picture.setTitle(photoDetail.getTitle());
    picture.setName(photoDetail.getName());
    picture.setSize(photoDetail.getFileSize());
    picture.setSizeH(photoDetail.getDefinition().getHeight());
    picture.setSizeL(photoDetail.getDefinition().getWidth());
    picture.setMimeType(photoDetail.getFileMimeType().getMimeType());
    picture.setInstance(photoDetail.getInstanceId());


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



    picture.setCommentsNumber(CommentServiceProvider.getCommentService().getCommentsCountOnPublication(CommentDTO.TYPE_PHOTO, new MediaPK(photoDetail.getId())));

    return picture;
  }

  @SuppressWarnings("deprecation")
  private String getBase64ImageData(String instanceId, Photo photoDetail, MediaResolution size) throws Exception {
    SettingBundle gallerySettings = ResourceLocator.getSettingBundle("org.silverpeas.gallery.settings.gallerySettings");

    String nomRep = gallerySettings.getString("imagesSubDirectory") + photoDetail.getMediaPK().getId();
    String[] rep = {nomRep};
    String path = FileRepositoryManager.getAbsolutePath(null, instanceId, rep);

    Media media = getGalleryService().getMedia(new MediaPK(photoDetail.getId(), instanceId));
    SilverpeasFile f = media.getFile(size);

    FileInputStream is = new FileInputStream(f);
    byte[] binaryData = new byte[(int) f.length()];
    is.read(binaryData);
    is.close();
    String data = "data:" + photoDetail.getFileMimeType().getMimeType() + ";base64," + new String(Base64.encodeBase64(binaryData));

    return data;
  }

  private GalleryService getGalleryService() throws Exception {
    return MediaServiceProvider.getMediaService();
  }
}
