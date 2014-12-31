package com.silverpeas.mobile.server.helpers;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.util.FileRepositoryManager;
import org.apache.commons.codec.binary.Base64;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author: svu
 */
public class DataURLHelper {

  /**
   * Return avatar dataurl
   * @param photoFileName
   * @return
   */
  public static String convertAvatarToUrlData(final String photoFileName) {
    String data = "";
    try {
      File f = new File(FileRepositoryManager.getAvatarPath() + File.separatorChar + photoFileName);
      FileInputStream is = new FileInputStream(f);
      byte[] binaryData = new byte[(int) f.length()];
      is.read(binaryData);
      is.close();
      MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
      data = "data:" + mimeTypesMap.getContentType(f) + ";base64," + new String(
          Base64.encodeBase64(binaryData));
    } catch (Exception e) {
      SilverTrace.error(SpMobileLogModule.getName(),
          "PublicationContentServlet.convertSpImageUrlToDataUrl", "root.EX_NO_MESSAGE", e);
    }
    return data;
  }

}
