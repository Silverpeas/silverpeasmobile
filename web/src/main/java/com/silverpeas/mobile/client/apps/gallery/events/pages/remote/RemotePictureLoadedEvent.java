package com.silverpeas.mobile.client.apps.gallery.events.pages.remote;

import java.util.List;

import com.silverpeas.mobile.shared.dto.gallery.PhotoDTO;


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
