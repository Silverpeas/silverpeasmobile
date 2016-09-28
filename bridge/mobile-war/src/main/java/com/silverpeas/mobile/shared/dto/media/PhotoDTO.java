package com.silverpeas.mobile.shared.dto.media;

import java.io.Serializable;

public class PhotoDTO extends MediaDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String dataPhoto;
  private String format;
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
