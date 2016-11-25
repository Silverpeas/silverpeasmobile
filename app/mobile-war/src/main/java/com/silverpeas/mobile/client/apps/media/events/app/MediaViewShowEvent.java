package com.silverpeas.mobile.client.apps.media.events.app;

import com.silverpeas.mobile.shared.dto.media.MediaDTO;

public class MediaViewShowEvent extends AbstractMediaAppEvent {

	private MediaDTO media;

	public MediaViewShowEvent(MediaDTO media) {
		super();
		this.media = media;
	}

	@Override
	protected void dispatch(MediaAppEventHandler handler) {
		handler.loadMediaShow(this);
	}

	public MediaDTO getMedia() {
    return media;
  }
}
