package com.silverpeas.mobile.shared.dto.media;

import java.io.Serializable;

public class VideoStreamingDTO extends MediaDTO implements Serializable {

  private String duration;
  private String url;

  private static final long serialVersionUID = 1L;

  public String getUrl() {
    return url;
  }

  public void setUrl(final String url) {
    this.url = url;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(final String duration) {
    this.duration = duration;
  }
}
