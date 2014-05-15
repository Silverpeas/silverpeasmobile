package com.silverpeas.mobile.client.apps.gallery.events.pages;

import java.util.List;

import com.silverpeas.mobile.client.apps.gallery.persistances.Picture;

public class GalleryLocalPicturesLoadedEvent extends AbstractGalleryPagesEvent {

	private List<Picture> pictures;
	
	public GalleryLocalPicturesLoadedEvent(List<Picture> pictures) {
		super();
		this.pictures = pictures;		
	}

	@Override
	protected void dispatch(GalleryPagesEventHandler handler) {
		handler.onLocalPicturesLoaded(this);
	}

	public List<Picture> getPictures() {
		return pictures;
	}
}
