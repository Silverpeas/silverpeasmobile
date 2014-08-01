package com.silverpeas.mobile.client.apps.media.events.pages.navigation;


public class MediaItemClickEvent extends AbstractMediaNavigationPagesEvent {

	private Object mediaItem;
	
	public MediaItemClickEvent(Object mediaItem) {
		super();
		this.mediaItem = mediaItem;
	}

	@Override
	protected void dispatch(MediaNavigationPagesEventHandler handler) {
		handler.onMediaItemClicked(this);
	}

	public Object getMediaItem() {
		return mediaItem;
	}
}
