package com.silverpeas.mobile.server.helpers;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.util.FileRepositoryManager;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.silverpeas.file.SilverpeasFile;
import org.silverpeas.file.SilverpeasFileProvider;

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
      SilverpeasFileProvider provider = SilverpeasFileProvider.getInstance();
      File originalImage = new File(FileRepositoryManager.getAvatarPath() + File.separatorChar + photoFileName);
      if (!originalImage.exists()) {
        return "";
      }

      String askedPath = originalImage.getParent() + File.separator + size + File.separator + photoFileName;
      SilverpeasFile image = provider.getSilverpeasFile(askedPath);

      byte[] binaryData = IOUtils.toByteArray(image.inputStream());
      MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
      data = "data:" + mimeTypesMap.getContentType(originalImage) + ";base64," + new String(
          Base64.encodeBase64(binaryData));
    } catch (Exception e) {
      SilverTrace.error(SpMobileLogModule.getName(),
          "PublicationContentServlet.convertSpImageUrlToDataUrl", "root.EX_NO_MESSAGE", e);
    }
    return data;
  }

}
