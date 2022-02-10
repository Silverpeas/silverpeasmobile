/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.server.helpers;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.silverpeas.core.io.file.SilverpeasFile;
import org.silverpeas.core.io.file.SilverpeasFileProvider;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.mobile.server.common.SpMobileLogModule;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

/**
 * @author: svu
 */
public class DataURLHelper {

  /**
   * Return avatar dataurl
   *
   * @param photoFileName
   * @return
   */
  public static String convertAvatarToUrlData(String photoFileName, String size) {

    int i = photoFileName.lastIndexOf("/");
    if (i == -1) {
      i = photoFileName.lastIndexOf("\\");
    }
    if (i != -1) {
      photoFileName = photoFileName.substring(i + 1);
    }

    String data = "";
    try {
      File originalImage =
          new File(FileRepositoryManager.getAvatarPath() + File.separatorChar + photoFileName);
      if (!originalImage.exists()) {
        return "";
      }

      String askedPath =
          originalImage.getParent() + File.separator + size + File.separator + photoFileName;
      SilverpeasFile image = SilverpeasFileProvider.getFile(askedPath);

      byte[] binaryData = IOUtils.toByteArray(image.inputStream());
      MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
      String typeMime = mimeTypesMap.getContentType(originalImage);
      if (typeMime.equalsIgnoreCase("jpeg")) {
        typeMime = "jpg";
      }

      data = "data:" + typeMime + ";base64," + new String(Base64.encodeBase64(binaryData));
    } catch (Exception e) {
      SilverLogger.getLogger(SpMobileLogModule.getName())
          .error("PublicationContentServlet.convertSpImageUrlToDataUrl", "root.EX_NO_MESSAGE", e);
    }
    return data;
  }

  public static String convertPictureToUrlData(String path, String fileName, String size) {
    String data = "";
    try {
      File originalImage = new File(path);
      if (!originalImage.exists()) {
        return "";
      }

      String askedPath;
      if (size == null) {
        askedPath = path + File.separator + fileName;
      } else {
        askedPath = originalImage.getParent() + File.separator + size + File.separator + fileName;
      }

      SilverpeasFile image = SilverpeasFileProvider.getFile(askedPath);

      byte[] binaryData = IOUtils.toByteArray(image.inputStream());
      MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
      String typeMime = mimeTypesMap.getContentType(originalImage);
      if (typeMime.equalsIgnoreCase("jpeg")) {
        typeMime = "jpg";
      }

      data = "data:" + typeMime + ";base64," + new String(Base64.encodeBase64(binaryData));
    } catch (Exception e) {
      SilverLogger.getLogger(SpMobileLogModule.getName())
          .error("PublicationContentServlet.convertSpImageUrlToDataUrl", "root.EX_NO_MESSAGE", e);
    }


    return data;
  }

}
