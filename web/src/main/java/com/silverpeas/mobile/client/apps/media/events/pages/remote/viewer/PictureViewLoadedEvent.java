package com.silverpeas.mobile.client.apps.media.events.pages.remote.viewer;

import com.silverpeas.mobile.shared.dto.media.PhotoDTO;


public class PictureViewLoadedEvent extends AbstractPictureViewerPageEvent {
		
	private PhotoDTO photo;
	
	public PictureViewLoadedEvent(PhotoDTO photo) {
		super();
		this.photo = photo;
	}

	@Override
	protected void dispatch(PicturesViewerPageEventHandler handler) {
		handler.onPictureLoaded(this);
	}

	public PhotoDTO getPhoto() {
		return photo;
	}
}
