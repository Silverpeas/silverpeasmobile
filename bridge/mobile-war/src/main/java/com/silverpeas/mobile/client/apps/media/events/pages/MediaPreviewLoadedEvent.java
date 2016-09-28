package com.silverpeas.mobile.client.apps.media.events.pages;


import com.silverpeas.mobile.shared.dto.media.MediaDTO;

public class MediaPreviewLoadedEvent extends AbstractMediaPagesEvent {

  private MediaDTO preview;
  private boolean commentable, notifiable;

  public MediaPreviewLoadedEvent(MediaDTO preview, boolean commentable, boolean notifiable) {
    super();
    this.preview = preview;
    this.commentable = commentable;
    this.notifiable = notifiable;
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

  public boolean isNotifiable() {
    return notifiable;
  }
}
