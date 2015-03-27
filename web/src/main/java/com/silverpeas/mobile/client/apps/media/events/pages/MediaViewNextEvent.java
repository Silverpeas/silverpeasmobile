package com.silverpeas.mobile.client.apps.media.events.pages;

import com.silverpeas.mobile.shared.dto.media.MediaDTO;

public class MediaViewNextEvent extends AbstractMediaPagesEvent {

  private MediaDTO nextMedia;

  public MediaViewNextEvent(MediaDTO nextMedia) {
    super();
    this.nextMedia = nextMedia;
  }

  @Override
  protected void dispatch(MediaPagesEventHandler handler) {
    handler.onMediaViewNext(this);
  }

  public MediaDTO getNextMedia() {
    return nextMedia;
  }
}
