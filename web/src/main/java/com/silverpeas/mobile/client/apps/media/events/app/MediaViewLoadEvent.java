package com.silverpeas.mobile.client.apps.media.events.app;

public class MediaViewLoadEvent extends AbstractMediaAppEvent {

	private String instanceId, mediaId;

	public MediaViewLoadEvent(String instanceId, String mediaId) {
		super();
		this.mediaId = mediaId;
		this.instanceId = instanceId;
	}

	@Override
	protected void dispatch(MediaAppEventHandler handler) {
		handler.loadMediaView(this);
	}

	public String getInstanceId() {
		return instanceId;
	}

  public String getMediaId() {
    return mediaId;
  }
}
