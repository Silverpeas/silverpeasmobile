package com.silverpeas.mobile.client.apps.media.events.pages;


import com.silverpeas.mobile.shared.dto.media.PhotoDTO;

public class MediaViewLoadedEvent extends AbstractMediaPagesEvent {

  private PhotoDTO view;

  public MediaViewLoadedEvent(PhotoDTO view) {
    super();
    this.view = view;
  }

  @Override
  protected void dispatch(MediaPagesEventHandler handler) {
    handler.onMediaViewLoaded(this);
  }

  public PhotoDTO getView() {
    return view;
  }
}
