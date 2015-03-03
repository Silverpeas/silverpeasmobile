package com.silverpeas.mobile.shared.dto.media;

import java.io.Serializable;

public class VideoDTO extends MediaDTO implements Serializable {

  private String duration;
  private String dataPoster;

  private static final long serialVersionUID = 1L;

  public String getDuration() {
    return duration;
  }

  public void setDuration(final String duration) {
    this.duration = duration;
  }

  public String getDataPoster() {
    return dataPoster;
  }

  public void setDataPoster(final String dataPoster) {
    this.dataPoster = dataPoster;
  }
}
