/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

package org.silverpeas.mobile.server.servlets;

import org.apache.commons.fileupload.FileItem;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.util.file.FileUploadUtil;
import org.silverpeas.core.web.http.HttpRequest;
import org.silverpeas.mobile.server.helpers.DataURLHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AvatarServlet extends AbstractSilverpeasMobileServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      checkUserInSession(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
    UserDetail user = getUserInSession(request);

    // Process the uploaded items
    List<FileItem> parameters = ((HttpRequest) request).getFileItems();
    FileItem file = FileUploadUtil.getFile(parameters, "upload_file0");
    ImageProfil img = new ImageProfil(user.getAvatarFileName());

    if (file != null && StringUtil.isDefined(file.getName())) {// Create or Update
      // extension
      String extension = FileRepositoryManager.getFileExtension(file.getName());
      if (extension != null && extension.equalsIgnoreCase("jpeg")) {
        extension = "jpg";
      }

      if (!"gif".equalsIgnoreCase(extension) && !"jpg".equalsIgnoreCase(extension) && !"png".
          equalsIgnoreCase(extension)) {
        response.sendError(response.SC_UNSUPPORTED_MEDIA_TYPE);
      }
      try (InputStream fis = file.getInputStream()) {
        img.saveImage(fis);
        user = Administration.get().getUserDetail(user.getId());
        String avatar = DataURLHelper.convertAvatarToUrlData(user.getAvatarFileName(), getSettings().getString("big.avatar.size", "40x"));
        response.addHeader("avatar", avatar);
      } catch (Exception e) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR);
      }
    }

  }
}
