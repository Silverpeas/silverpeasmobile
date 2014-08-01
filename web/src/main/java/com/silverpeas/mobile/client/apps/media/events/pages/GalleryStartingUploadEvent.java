package com.silverpeas.mobile.client.apps.media.events.pages;


public class GalleryStartingUploadEvent extends AbstractGalleryPagesEvent {

	private int picturesNumber;
	
	public GalleryStartingUploadEvent(int picturesNumber) {
		super();
		this.picturesNumber = picturesNumber;
	}

	@Override
	protected void dispatch(GalleryPagesEventHandler handler) {
		handler.onStartingUpload(this);
	}

	public int getPicturesNumber() {
		return picturesNumber;
	}
}
