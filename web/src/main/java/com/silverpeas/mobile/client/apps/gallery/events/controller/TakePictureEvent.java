package com.silverpeas.mobile.client.apps.gallery.events.controller;


public class TakePictureEvent extends AbstractGalleryControllerEvent {
	
	public TakePictureEvent() {
		super();	
	}

	@Override
	protected void dispatch(GalleryControllerEventHandler handler) {
		handler.takePicture(this);
	}
}
