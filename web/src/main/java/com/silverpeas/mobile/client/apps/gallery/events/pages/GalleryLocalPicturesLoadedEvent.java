package com.silverpeas.mobile.client.apps.gallery.events.pages;

import com.silverpeas.mobile.client.apps.gallery.persistances.Picture;

public class GalleryLocalPicturesLoadedEvent extends AbstractGalleryPagesEvent {

	private Picture[] pictures;
	
	public GalleryLocalPicturesLoadedEvent(Picture[] pictures) {
		super();
		this.pictures = pictures;		
	}

	@Override
	protected void dispatch(GalleryPagesEventHandler handler) {
		handler.onLocalPicturesLoaded(this);
	}

	public Picture[] getPictures() {
		return pictures;
	}
}
