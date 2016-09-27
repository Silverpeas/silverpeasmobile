package com.silverpeas.mobile.server.helpers;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.silverpeas.core.io.file.SilverpeasFile;
import org.silverpeas.core.io.file.SilverpeasFileProvider;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.util.logging.SilverLogger;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

/**
 * @author: svu
 */
public class DataURLHelper {

  /**
   * Return avatar dataurl
   * @param photoFileName
   * @return
   */
  public static String convertAvatarToUrlData(final String photoFileName, String size) {
    String data = "";
    try {
      File originalImage = new File(FileRepositoryManager.getAvatarPath() + File.separatorChar + photoFileName);
      if (!originalImage.exists()) {
        return "";
      }

      String askedPath = originalImage.getParent() + File.separator + size + File.separator + photoFileName;
      SilverpeasFile image = SilverpeasFileProvider.getFile(askedPath);

      byte[] binaryData = IOUtils.toByteArray(image.inputStream());
      MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
      data = "data:" + mimeTypesMap.getContentType(originalImage) + ";base64," + new String(
          Base64.encodeBase64(binaryData));
    } catch (Exception e) {
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("PublicationContentServlet.convertSpImageUrlToDataUrl", "root.EX_NO_MESSAGE", e);
    }
    return data;
  }

}
