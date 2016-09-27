package com.silverpeas.mobile.client.apps.media.events.app;

import com.silverpeas.mobile.shared.dto.media.MediaDTO;

public class MediaPreviewLoadEvent extends AbstractMediaAppEvent {

  private String instanceId, mediaId, contentType;
  private MediaDTO media;

  public MediaPreviewLoadEvent(String instanceId, String contentType, String mediaId, MediaDTO media) {
    super();
    this.mediaId = mediaId;
    this.contentType = contentType;
    this.instanceId = instanceId;
    this.media = media;
  }

  @Override
  protected void dispatch(MediaAppEventHandler handler) {
    handler.loadMediaPreview(this);
  }

  public String getInstanceId() {
    return instanceId;
  }

  public String getMediaId() {
    return mediaId;
  }

  public MediaDTO getMedia() {
    return media;
  }

  public String getContentType() {
    return contentType;
  }
}
