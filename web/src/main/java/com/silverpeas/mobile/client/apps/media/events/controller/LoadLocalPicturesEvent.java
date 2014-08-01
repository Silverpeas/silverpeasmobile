package com.silverpeas.mobile.client.apps.media.events.controller;


public class LoadLocalPicturesEvent extends AbstractGalleryControllerEvent {
	
	public LoadLocalPicturesEvent() {
		super();
	}

	@Override
	protected void dispatch(GalleryControllerEventHandler handler) {
		handler.loadLocalPictures(this);
	}
}
