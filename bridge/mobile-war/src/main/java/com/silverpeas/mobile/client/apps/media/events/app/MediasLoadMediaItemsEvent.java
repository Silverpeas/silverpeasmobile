package com.silverpeas.mobile.client.apps.media.events.app;

public class MediasLoadMediaItemsEvent extends AbstractMediaAppEvent {

	private String rootAlbumId, instanceId;

	public MediasLoadMediaItemsEvent(String instanceId, String rootTopicId) {
		super();
		this.rootAlbumId = rootTopicId;
		this.instanceId = instanceId;
	}

	@Override
	protected void dispatch(MediaAppEventHandler handler) {
		handler.loadAlbums(this);
	}

	public String getRootAlbumId() {
		return rootAlbumId;
	}

	public String getInstanceId() {
		return instanceId;
	}
}
