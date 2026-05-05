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

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.util.file.FileItem;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.util.file.FileUploadUtil;
import org.silverpeas.core.web.rs.UserPrivilegeValidation;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.kernel.util.StringUtil;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.server.helpers.ImageProfil;

import java.io.InputStream;
import java.util.List;

@WebService
@Authorized
@Path(ServiceAvatar.PATH)
public class ServiceAvatar extends AbstractRestWebService {

  static final String PATH = "mobile/avatar";

  @POST
  @Path("")
  public void updateAvatar() throws Exception {
    UserDetail user = Administration.get().getUserDetail(getUser().getId());

    // Process the uploaded items
    List<FileItem> parameters =  getHttpRequest().getFileItems();
    FileItem file = FileUploadUtil.getFile(parameters, "upload_file0");
    ImageProfil img = new ImageProfil(user.getAvatarFileName());

    if (file != null && StringUtil.isDefined(file.getFileName())) {// Create or Update
      // extension
      String extension = FileRepositoryManager.getFileExtension(file.getFileName());
      if (extension != null && extension.equalsIgnoreCase("jpeg")) {
        extension = "jpg";
      }

      if (!"gif".equalsIgnoreCase(extension) && !"jpg".equalsIgnoreCase(extension) && !"png".
          equalsIgnoreCase(extension)) {
        throw new WebApplicationException(Response.Status.UNSUPPORTED_MEDIA_TYPE);
      }
      try (InputStream fis = file.getInputStream()) {
        img.saveImage(fis);

        String avatar = DataURLHelper.convertAvatarToUrlData(user.getAvatarFileName(), getSettings().getString("big.avatar.size", "40x"));

        getHttpServletResponse().addHeader("avatar", avatar);
      } catch (Exception e) {
        throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
      }
    }
}

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return null;
  }

  @Override
  public void validateUserAuthorization(final UserPrivilegeValidation validation) {
    // no validation to perform
  }

}
