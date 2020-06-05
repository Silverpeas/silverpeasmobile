/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.server.common;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LocalDiskFileItem extends DiskFileItem {

  private static final long serialVersionUID = 1L;
  private File file;

  @Override
  public InputStream getInputStream() throws IOException {
    InputStream inputStream = new FileInputStream(file.getAbsolutePath());
    return inputStream;
  }

  public LocalDiskFileItem(File file, String contentType) {
    super("WAIMGVAR0", contentType, false, file.getName(), 2097152, file);
    this.file = file;
  }

  @Override
  public boolean isInMemory() {
    return false;
  }

  @Override
  public long getSize() {
    return file.length();
  }

  @Override
  public byte[] get() {
    try {
      return IOUtils.toByteArray(new FileInputStream(file.getAbsolutePath()));
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public String getFieldName() {
    return "WAIMGVAR0";
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    OutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
    return outputStream;
  }

}
