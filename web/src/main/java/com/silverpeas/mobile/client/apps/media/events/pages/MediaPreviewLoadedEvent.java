package com.silverpeas.mobile.client.apps.media.events.pages;


import com.silverpeas.mobile.shared.dto.media.MediaDTO;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;

public class MediaPreviewLoadedEvent extends AbstractMediaPagesEvent {

  private MediaDTO preview;
  private boolean commentable;

  public MediaPreviewLoadedEvent(MediaDTO preview, boolean commentable) {
    super();
    this.preview = preview;
    this.commentable = commentable;
  }

  @Override
  protected void dispatch(MediaPagesEventHandler handler) {
    handler.onMediaPreviewLoaded(this);
  }

  public MediaDTO getPreview() {
    return preview;
  }

  public boolean isCommentable() {
    return commentable;
  }
}
