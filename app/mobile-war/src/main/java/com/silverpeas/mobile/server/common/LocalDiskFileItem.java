package com.silverpeas.mobile.server.common;

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
