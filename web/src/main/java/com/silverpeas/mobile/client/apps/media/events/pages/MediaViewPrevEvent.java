package com.silverpeas.mobile.client.apps.media.events.pages;

import com.silverpeas.mobile.shared.dto.media.MediaDTO;

public class MediaViewPrevEvent extends AbstractMediaPagesEvent {

  private MediaDTO prevMedia;

  public MediaViewPrevEvent(MediaDTO prevMedia) {
    super();
    this.prevMedia = prevMedia;
  }

  @Override
  protected void dispatch(MediaPagesEventHandler handler) {
    handler.onMediaViewPrev(this);
  }

  public MediaDTO getPrevMedia() {
    return prevMedia;
  }
}
