package com.silverpeas.mobile.client.apps.media.events.pages;


import com.silverpeas.mobile.shared.dto.media.PhotoDTO;

public class MediaPreviewLoadedEvent extends AbstractMediaPagesEvent {

  private PhotoDTO preview;

	public MediaPreviewLoadedEvent(PhotoDTO preview) {
		super();
    this.preview = preview;
	}

	@Override
	protected void dispatch(MediaPagesEventHandler handler) {
		handler.onMediaPreviewLoaded(this);
	}

  public PhotoDTO getPreview() {
    return preview;
  }
}
