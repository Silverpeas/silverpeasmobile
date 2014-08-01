package com.silverpeas.mobile.client.apps.media.events.controller;


public class TakePictureEvent extends AbstractGalleryControllerEvent {
	
	private String imageData;
	
	public TakePictureEvent(String imageData) {
		super();	
		this.imageData = imageData;
	}

	@Override
	protected void dispatch(GalleryControllerEventHandler handler) {
		handler.takePicture(this);
	}

	public String getImageData() {
		return imageData;
	}
}
