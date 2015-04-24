package com.silverpeas.mobile.server.helpers;

import com.silverpeas.mobile.server.common.QtFastStart;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author: svu
 */
public class MediaHelper {

  public static String DIR_UPLOAD = "media";
  private static String DIR_PREFIX = "webopti";

  public static File optimizeVideoForWeb(File f, String uuid) {
    File dir = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + DIR_PREFIX + uuid);
    dir.mkdir();
    File opti = new File(dir.getAbsolutePath() + System.getProperty("file.separator") + f.getName());
    try {
      boolean optimized = QtFastStart.fastStart(f, opti);
      if (!optimized) return f;
    } catch (Exception e) {
      e.printStackTrace();
      return f;
    }
    return opti;
  }

  public static String getTemporaryUploadMediaPath() {
    File dir = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + DIR_UPLOAD);
    dir.mkdir();
    return dir.getAbsolutePath();
  }

  public static File optimizeVideoForWeb(File f) {
    return optimizeVideoForWeb(f, "");
  }

  public static void cleanTemporaryFiles(String uuid) {
    String dir = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + DIR_PREFIX + uuid;
    String dir2 = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + DIR_UPLOAD;
    try {
      FileUtils.deleteDirectory(new File(dir));
      FileUtils.deleteDirectory(new File(dir2));
    } catch (IOException e) {}
  }
}
