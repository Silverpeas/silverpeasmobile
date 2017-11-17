/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package org.silverpeas.mobile.server.servlets;

import org.apache.commons.io.IOUtils;
import org.silverpeas.components.gallery.constant.MediaResolution;
import org.silverpeas.components.gallery.model.Media;
import org.silverpeas.components.gallery.model.MediaPK;
import org.silverpeas.components.gallery.model.Sound;
import org.silverpeas.components.gallery.service.GalleryService;
import org.silverpeas.components.gallery.service.MediaServiceProvider;
import org.silverpeas.core.io.file.SilverpeasFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("serial")
public class SoundServlet extends AbstractSilverpeasMobileServlet {

  private static final int BUFFER_LENGTH = 9000;
  private static final long EXPIRE_TIME = 1000 * 60 * 60; // one hour

  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

    String id = request.getParameter("id");
    String action = request.getParameter("action");
    Media sound = getGalleryService().getMedia(new MediaPK(id)).getSound();
    if (sound.canBeAccessedBy(getUserInSession(request))) {
      SilverpeasFile f = sound.getFile(MediaResolution.ORIGINAL);
      if (action != null && action.equalsIgnoreCase("download")) {
        // download sound
        response.setContentType(((Sound) sound).getFileMimeType().getMimeType());
        response.setHeader("content-disposition", "attachment; filename=" + sound.getName());
        IOUtils.copy(f.inputStream(), response.getOutputStream());
      } else {
        // send sound on streaming
        String range = request.getHeader("Range");
        if (range == null) range = "bytes=0-";
        long length = f.length();
        long start = 0;
        long end = length - 1;

        String[] ranges = range.substring("bytes=".length()).split("-");
        start = Integer.valueOf(ranges[0]);
        if (ranges.length == 2) {
          end = Integer.valueOf(ranges[1]);
        }
        if (end > length - 1) end = length - 1;
        long contentLength = end - start + 1;
        long lastModified = f.lastModified();

        response.reset();
        response.resetBuffer();
        response.setBufferSize(BUFFER_LENGTH);
        response.setHeader("Content-Disposition", String.format("inline;filename=\"%s\"", f.getName()));
        response.setHeader("Accept-Ranges", "bytes");
        response.setDateHeader("Expires", System.currentTimeMillis() + EXPIRE_TIME);
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setHeader("Content-Range", String.format("bytes %s-%s/%s", start, end, length));
        response.setHeader("Content-Length", String.format("%s", contentLength));
        response.setContentType(f.getMimeType());
        response.setDateHeader("Last-Modified", lastModified);

        InputStream in = f.inputStream();
        OutputStream out = response.getOutputStream();
        response.setContentLength((int) contentLength);
        response.setBufferSize(BUFFER_LENGTH);
        int bytesRead = 0;
        int bytesLeft = (int) contentLength;
        byte[] buffer = new byte[BUFFER_LENGTH];
        in.mark((int) start);
        for (; ; ) {
          bytesRead = in.read(buffer);
          if (bytesRead == -1 || bytesLeft <= 0) {
            break;
          }
          out.write(buffer, 0, bytesLeft < bytesRead ? bytesLeft : bytesRead);
          bytesLeft -= bytesRead;
        }
      }
    } else {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
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

  private GalleryService getGalleryService() throws Exception {
    return MediaServiceProvider.getMediaService();
  }
}
