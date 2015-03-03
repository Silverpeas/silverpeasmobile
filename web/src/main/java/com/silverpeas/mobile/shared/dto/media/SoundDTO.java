package com.silverpeas.mobile.shared.dto.media;

import java.io.Serializable;

public class SoundDTO extends MediaDTO implements Serializable {

  private String duration;

  private static final long serialVersionUID = 1L;

  public String getDuration() {
    return duration;
  }

  public void setDuration(final String duration) {
    this.duration = duration;
  }
}
