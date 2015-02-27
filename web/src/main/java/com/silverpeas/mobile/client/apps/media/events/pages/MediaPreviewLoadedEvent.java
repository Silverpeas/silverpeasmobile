package com.silverpeas.mobile.client.apps.media.events.pages;


import com.silverpeas.mobile.shared.dto.media.MediaDTO;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;

public class MediaPreviewLoadedEvent extends AbstractMediaPagesEvent {

  private PhotoDTO preview;
  private boolean commentable;

	public MediaPreviewLoadedEvent(PhotoDTO preview, boolean commentable) {
		super();
    this.preview = preview;
    this.commentable = commentable;
	}

	@Override
	protected void dispatch(MediaPagesEventHandler handler) {
		handler.onMediaPreviewLoaded(this);
	}

  public PhotoDTO getPreview() {
    return preview;
  }

  public boolean isCommentable() {
    return commentable;
  }
}
