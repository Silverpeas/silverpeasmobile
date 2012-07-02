package com.silverpeas.mobile.client.apps.gallery.events.pages;


public class GalleryEndUploadEvent extends AbstractGalleryPagesEvent {
	
	public GalleryEndUploadEvent() {
		super();
	}

	@Override
	protected void dispatch(GalleryPagesEventHandler handler) {
		handler.onEndUpload(this);
	}
}
