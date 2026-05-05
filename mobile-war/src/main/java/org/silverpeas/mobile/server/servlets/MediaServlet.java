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

import jakarta.activation.MimetypesFileTypeMap;
import jakarta.inject.Inject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.silverpeas.core.admin.user.model.User;
import org.silverpeas.core.node.model.NodePK;
import org.silverpeas.core.util.file.FileItem;
import org.silverpeas.core.util.file.FileUploadSizeLimitException;
import org.silverpeas.core.util.file.FileUploadUtil;
import org.silverpeas.kernel.SilverpeasRuntimeException;
import org.silverpeas.mobile.server.common.LocalDiskFileItem;
import org.silverpeas.mobile.server.helpers.MediaHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MediaServlet extends AbstractSilverpeasMobileServlet {

  @Inject
  private GalleryService galleryService;

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
  }

  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String action = request.getParameter("action");
    if (action.equals("view")) {
      String id = request.getParameter("id");
      Photo photo = getPicture(id);
      if (photo.canBeAccessedBy(getUserInSession(request))) {
        InputStream input = photo.getFile(MediaResolution.ORIGINAL).inputStream();
        response.setContentType((photo).getFileMimeType().getMimeType());
        response.setHeader("content-disposition", "attachment; filename=" + photo.getName());
        IOUtils.copy(input, response.getOutputStream());
      } else {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
      }
    }
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    try {
      checkUserInSession(request, response);
      processRequest(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String componentId = "";
    String albumId = "";
    String tempDir = MediaHelper.getTemporaryUploadMediaPath();

    List<FileItem> items;
    try {
      items = FileUploadUtil.parseRequest(request);
    } catch(FileUploadSizeLimitException e) {
      response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
      return;
    } catch (SilverpeasRuntimeException e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    // Process the uploaded items
    for (FileItem item : items) {
      if (item.isFormField()) {
        if (item.getFieldName().equals("componentId")) componentId = item.getContent();
        if (item.getFieldName().equals("albumId")) albumId = item.getContent();

      } else {
        String fileName = item.getFileName();
        File file = new File(tempDir + File.separator + fileName);
        try {
          item.saveTo(file);
          if (scanAntivirus(response, file)) return;
          createMedia(request, response, componentId, albumId, file);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private Photo getPicture(String pictureId) {
    return getGalleryService().getPhoto(new MediaPK(pictureId));
  }

  private GalleryService getGalleryService() {
    return galleryService;
  }

  private void createMedia(HttpServletRequest request, HttpServletResponse response, String componentId,
      String albumId, File file)
      throws Exception {

    // création de la photo
    String type = new MimetypesFileTypeMap().getContentType(file);

    List<FileItem> parameters = new ArrayList<>();
    LocalDiskFileItem item = new LocalDiskFileItem(file, type);
    parameters.add(item);


    MediaDataCreateDelegate delegate = null;
    if (type.contains("image")) {
      delegate = new MediaDataCreateDelegate(MediaType.Photo, "fr", albumId, parameters);
    } else if (type.contains("audio")) {
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
      return;
    }

    Media newMedia = getGalleryService().createMedia(getUserInSession(request), componentId, GalleryComponentSettings.getWatermark(componentId), delegate);

    // notification management
    User sender = getUserInSession(request);
    GalleryUserAlertNotification n = new GalleryUserAlertNotification(new NodePK(albumId, componentId), newMedia, sender);
    n.build().send();

    newMedia.getId();
  }
}
