package com.silverpeas.mobile.client.apps.media.events.app;

import com.silverpeas.mobile.shared.dto.media.MediaDTO;

public class MediaViewGetNextEvent extends AbstractMediaAppEvent {

	private MediaDTO currentMedia;

	public MediaViewGetNextEvent(MediaDTO currentMedia) {
		super();
		this.currentMedia = currentMedia;
	}

	@Override
	protected void dispatch(MediaAppEventHandler handler) {
		handler.nextMediaView(this);
	}

	public MediaDTO getCurrentMedia() {
		return currentMedia;
	}
}
