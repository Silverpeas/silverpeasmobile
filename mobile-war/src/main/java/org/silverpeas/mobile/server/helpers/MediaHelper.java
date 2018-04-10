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

package org.silverpeas.mobile.server.helpers;

import org.apache.commons.io.FileUtils;
import org.silverpeas.mobile.server.common.QtFastStart;

import java.io.File;
import java.io.IOException;

/**
 * @author: svu
 */
public class MediaHelper {

  public static final String DIR_UPLOAD = "media";
  private static final String DIR_PREFIX = "webopti";

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
