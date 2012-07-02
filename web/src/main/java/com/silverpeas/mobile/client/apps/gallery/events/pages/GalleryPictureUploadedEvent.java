package com.silverpeas.mobile.client.apps.gallery.events.pages;


public class GalleryPictureUploadedEvent extends AbstractGalleryPagesEvent {
	
	public GalleryPictureUploadedEvent() {
		super();
	}

	@Override
	protected void dispatch(GalleryPagesEventHandler handler) {
		handler.onPictureUploaded(this);
	}
}
