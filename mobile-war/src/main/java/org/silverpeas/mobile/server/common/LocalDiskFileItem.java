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

package org.silverpeas.mobile.server.common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.silverpeas.core.util.file.FileItem;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class LocalDiskFileItem implements FileItem, Serializable {

  private static final long serialVersionUID = 1L;
  private final File file;
  private final String contentType;

  @Override
  public InputStream getInputStream() throws IOException {
    return new FileInputStream(file);
  }

  public LocalDiskFileItem(File file, String contentType) {
    this.file = file;
    this.contentType = contentType;
  }

  @Override
  public long getSize() {
    return file.length();
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public void saveTo(File file) throws IOException {
    FileUtils.copyInputStreamToFile(getInputStream(), file);
  }

  @Override
  public void delete() throws IOException {
    Files.delete(file.toPath());
  }

  @Override
  public boolean isFormField() {
    return false;
  }

  @Override
  public String getFieldName() {
    return "WAIMGVAR0";
  }

  @Override
  public String getContent(Charset charset) {
    return new String(read(), charset);
  }

  @Override
  public String getContent() {
    return new String(read());
  }

  @Override
  public String getFileName() {
    return file.getName();
  }

  private byte[] read() {
    byte[] data = new byte[(int) getSize()];
    try (InputStream input = getInputStream()) {
      IOUtils.readFully(input, data);
    } catch (IOException e) {
      data = null;
    }
    return data;
  }

}
