package com.silverpeas.mobile.shared.dto.media;

import com.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

public class PhotoDTO extends MediaDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String dataPhoto;
  private String format;
  private long size;
  private int sizeH;
  private int sizeL;

  public String getDataPhoto() {
    return dataPhoto;
  }
  public void setDataPhoto(String dataPhoto) {
    this.dataPhoto = dataPhoto;
  }
  public String getFormat() {
    return format;
  }
  public void setFormat(String format) {
    this.format = format;
  }

  public void setSize(final long size) {
    this.size = size;
  }

  public long getSize() {
    return size;
  }

  public void setSizeH(final int sizeH) {
    this.sizeH = sizeH;
  }

  public int getSizeH() {
    return sizeH;
  }

  public void setSizeL(final int sizeL) {
    this.sizeL = sizeL;
  }

  public int getSizeL() {
    return sizeL;
  }
}
