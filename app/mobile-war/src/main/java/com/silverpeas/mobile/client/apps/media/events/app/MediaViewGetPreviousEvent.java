package com.silverpeas.mobile.client.apps.media.events.app;

import com.silverpeas.mobile.shared.dto.media.MediaDTO;

public class MediaViewGetPreviousEvent extends AbstractMediaAppEvent {

	private MediaDTO currentMedia;

	public MediaViewGetPreviousEvent(MediaDTO currentMedia) {
		super();
		this.currentMedia = currentMedia;
	}

	@Override
	protected void dispatch(MediaAppEventHandler handler) {
		handler.prevMediaView(this);
	}

	public MediaDTO getCurrentMedia() {
		return currentMedia;
	}
}
