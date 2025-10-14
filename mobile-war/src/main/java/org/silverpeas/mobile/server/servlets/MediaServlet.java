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

package org.silverpeas.mobile.server.servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.silverpeas.components.gallery.GalleryComponentSettings;
import org.silverpeas.components.gallery.constant.MediaResolution;
import org.silverpeas.components.gallery.constant.MediaType;
import org.silverpeas.components.gallery.delegate.MediaDataCreateDelegate;
import org.silverpeas.components.gallery.model.Media;
import org.silverpeas.components.gallery.model.MediaPK;
import org.silverpeas.components.gallery.model.Photo;
import org.silverpeas.components.gallery.notification.user.GalleryUserAlertNotification;
import org.silverpeas.components.gallery.service.GalleryService;
import org.silverpeas.components.gallery.service.MediaServiceProvider;
import org.silverpeas.core.admin.user.model.User;
import org.silverpeas.core.node.model.NodePK;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.mobile.server.common.LocalDiskFileItem;
import org.silverpeas.mobile.server.helpers.MediaHelper;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class MediaServlet extends AbstractSilverpeasMobileServlet {

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
      if (photo.canBeAccessedBy(getUserInSession(request))) {
        InputStream input = photo.getFile(MediaResolution.ORIGINAL).inputStream();
        response.setContentType(((Photo) photo).getFileMimeType().getMimeType());
        response.setHeader("content-disposition", "attachment; filename=" + photo.getName());
        IOUtils.copy(input, response.getOutputStream());
      } else {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
      }
    }
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      checkUserInSession(request, response);
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

  private GalleryService getGalleryService() throws Exception {
    return MediaServiceProvider.getMediaService();
  }

  private String createMedia(HttpServletRequest request, HttpServletResponse response, String name, String userId, String componentId,
      String albumId, File file, boolean watermark, String watermarkHD,
      String watermarkOther, boolean download)
      throws Exception {

    // création de la photo
    String type = new MimetypesFileTypeMap().getContentType(file);

    List<FileItem> parameters = new ArrayList<FileItem>();
    LocalDiskFileItem item = new LocalDiskFileItem(file, type);
    parameters.add(item);


    MediaDataCreateDelegate delegate = null;
    if (type.contains("image")) {
      delegate = new MediaDataCreateDelegate(MediaType.Photo, "fr", albumId, parameters);
    } if (type.contains("audio")) {
      delegate = new MediaDataCreateDelegate(MediaType.Sound, "fr", albumId, parameters);
    } else if (type.contains("video")) {
      parameters.clear();
      parameters.add(new LocalDiskFileItem(
          MediaHelper.optimizeVideoForWeb(file, request.getSession().getId()), type));
      delegate = new MediaDataCreateDelegate(MediaType.Video, "fr", albumId, parameters);
    } else if (type.contains("octet-stream")) {
      if (file.getName().endsWith(".mp3")) {
        delegate = new MediaDataCreateDelegate(MediaType.Sound, "fr", albumId, parameters);
      } else if(file.getName().endsWith(".mp4")) {
        parameters.clear();
        parameters.add(new LocalDiskFileItem(
            MediaHelper.optimizeVideoForWeb(file, request.getSession().getId()), type));
        delegate = new MediaDataCreateDelegate(MediaType.Video, "fr", albumId, parameters);
      }
    }
    //TODO : use right language
    if (delegate == null) {
      response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
      return null;
    }

    Media newMedia = getGalleryService().createMedia(getUserInSession(request), componentId, GalleryComponentSettings.getWatermark(componentId), delegate);

    // notification management
    User sender = getUserInSession(request);
    GalleryUserAlertNotification n = new GalleryUserAlertNotification(new NodePK(albumId, componentId), newMedia, sender);
    n.build().send();

    return newMedia.getId();
  }
}
