package com.silverpeas.mobile.client.apps.media.events.pages.remote;

import java.util.List;

import com.silverpeas.mobile.shared.dto.media.PhotoDTO;


public class RemotePictureLoadedEvent extends AbstractRemotePicturesPageEvent {
		
	private List<PhotoDTO> photos;
	
	public RemotePictureLoadedEvent(List<PhotoDTO> photos) {
		super();
		this.photos = photos;
	}

	@Override
	protected void dispatch(RemotePicturesPageEventHandler handler) {
		handler.onPicturesLoaded(this);
	}

	public List<PhotoDTO> getPhotos() {
		return photos;
	}
}
