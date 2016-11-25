package com.silverpeas.mobile.server.servlets;

import com.silverpeas.mobile.server.common.LocalDiskFileItem;
import com.silverpeas.mobile.server.helpers.MediaHelper;
import com.silverpeas.mobile.server.services.AbstractAuthenticateService;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.silverpeas.components.gallery.constant.MediaType;
import org.silverpeas.components.gallery.delegate.MediaDataCreateDelegate;
import org.silverpeas.components.gallery.model.Media;
import org.silverpeas.components.gallery.model.MediaPK;
import org.silverpeas.components.gallery.model.Photo;
import org.silverpeas.components.gallery.service.GalleryService;
import org.silverpeas.components.gallery.service.MediaServiceProvider;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.file.FileRepositoryManager;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class MediaServlet extends HttpServlet {

  private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
  private static long MAX_FILE_SIZE      = 1024 * 1024 * 100; // 100MB
  private static long MAX_REQUEST_SIZE   = 1024 * 1024 * 110; // 110MB

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
    MAX_FILE_SIZE = FileRepositoryManager.getUploadMaximumFileSize();
    MAX_REQUEST_SIZE = (long) (MAX_FILE_SIZE * 1.1);
  }

  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String action = request.getParameter("action");
    if (action.equals("view")) {
      String id = request.getParameter("id");
      String instanceId = request.getParameter("instanceId");
      Photo photo = getPicture(id);
      getFile(response, instanceId, photo);
      ((OutputStream) response.getOutputStream()).flush();
    }
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      checkUserInSession(request);
      processRequest(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String componentId = "";
    String albumId = "";
    String tempDir = MediaHelper.getTemporaryUploadMediaPath();

    // configures upload settings
    DiskFileItemFactory factory = new DiskFileItemFactory();
    // sets memory threshold - beyond which files are stored in disk
    factory.setSizeThreshold(MEMORY_THRESHOLD);
    // sets temporary location to store files
    factory.setRepository(new File(tempDir));

    ServletFileUpload upload = new ServletFileUpload(factory);

    // sets maximum size of upload file
    upload.setFileSizeMax(MAX_FILE_SIZE);

    // sets maximum size of request (include file + form data)
    upload.setSizeMax(MAX_REQUEST_SIZE);

    // Parse the request
    @SuppressWarnings("unchecked")
    List<FileItem> items = null;
    try {
      items = upload.parseRequest(request);
    } catch(FileUploadBase.FileSizeLimitExceededException eu) {
      response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
      return;
    } catch (FileUploadException e) {
      e.printStackTrace();
    }

    // Process the uploaded items
    Iterator iter = items.iterator();
    while (iter.hasNext())
    {
      FileItem item = (FileItem) iter.next();
      if (item.isFormField())
      {
        if (item.getFieldName().equals("componentId")) componentId = item.getString();
        if (item.getFieldName().equals("albumId")) albumId = item.getString();

      }
      else {
        String fileName = item.getName();
        File file = new File(tempDir + File.separator + fileName);
        try {
          item.write(file);
          createMedia(request, response, fileName, getUserInSession(request).getId(), componentId, albumId, file, false, "", "", true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private Photo getPicture(String pictureId) throws Exception {
    Photo photoDetail = getGalleryService().getPhoto(new MediaPK(pictureId));
    return photoDetail;
  }

  protected void checkUserInSession(HttpServletRequest request) throws AuthenticationException {
    if (request.getSession().getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME) == null) {
      throw new AuthenticationException(AuthenticationException.AuthenticationError.NotAuthenticate);
    }
  }

  public void getFile(HttpServletResponse response, String instanceId, Photo photo) {
    InputStream input = null;

    try {
      SettingBundle gallerySettings = ResourceLocator.getSettingBundle("com.silverpeas.gallery.settings.gallerySettings");
      String nomRep = gallerySettings.getString("imagesSubDirectory") + photo.getMediaPK().getId();
      String[] rep = {nomRep};
      String path = FileRepositoryManager.getAbsolutePath(null, instanceId, rep);
      File f = new File(path+ photo.getFileName());


      FileInputStream is = new FileInputStream(f);
      byte[] binaryData = new byte[(int) f.length()];
      is.read(binaryData);
      is.close();

      input = new ByteArrayInputStream(binaryData);
      response.setContentType(photo.getFileMimeType().getMimeType());
      response.setHeader("content-disposition", "attachment; filename=" + photo.getFileName());

      byte[] buffer = new byte[1024];
      int read;
      while ((read = input.read(buffer)) > 0) {
        ((OutputStream) response.getOutputStream()).write(buffer, 0, read);
      }
    } catch (IOException e) {
      System.out.println("Error while trying to download the media.");
      e.printStackTrace();
    }
  }

  private GalleryService getGalleryService() throws Exception {
    return MediaServiceProvider.getMediaService();
  }

  private String createMedia(HttpServletRequest request, HttpServletResponse response, String name, String userId, String componentId,
      String albumId, File file, boolean watermark, String watermarkHD,
      String watermarkOther, boolean download)
      throws Exception {

    // création de la photo
    List<FileItem> parameters = new ArrayList<FileItem>();
    LocalDiskFileItem item = new LocalDiskFileItem(file);
    parameters.add(item);

    String type = new MimetypesFileTypeMap().getContentType(file);
    MediaDataCreateDelegate delegate = null;
    if (type.contains("image")) {
      delegate = new MediaDataCreateDelegate(MediaType.Photo, "fr", albumId, parameters);
    } if (type.contains("audio")) {
      delegate = new MediaDataCreateDelegate(MediaType.Sound, "fr", albumId, parameters);
    } else if (type.contains("video")) {
      parameters.clear();
      parameters.add(new LocalDiskFileItem(
          MediaHelper.optimizeVideoForWeb(file, request.getSession().getId())));
      delegate = new MediaDataCreateDelegate(MediaType.Video, "fr", albumId, parameters);
    } else if (type.contains("octet-stream")) {
      if (file.getName().endsWith(".mp3")) {
        delegate = new MediaDataCreateDelegate(MediaType.Sound, "fr", albumId, parameters);
      } else if(file.getName().endsWith(".mp4")) {
        parameters.clear();
        parameters.add(new LocalDiskFileItem(
            MediaHelper.optimizeVideoForWeb(file, request.getSession().getId())));
        delegate = new MediaDataCreateDelegate(MediaType.Video, "fr", albumId, parameters);
      }
    }
    //TODO : use right language
    if (delegate == null) {
      response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
      return null;
    }

    Media newMedia = getGalleryService().createMedia(getUserInSession(request), componentId, watermark, watermarkHD, watermarkOther, delegate);

    return newMedia.getId();
  }

  protected UserDetail getUserInSession(HttpServletRequest request) {
    return (UserDetail) request.getSession().getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME);
  }

}
