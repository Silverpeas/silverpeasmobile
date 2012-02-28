package com.silverpeas.mobile.client.apps.gallery.events.controller;


public class DeleteLocalPictureEvent extends AbstractGalleryControllerEvent {
	
	private String id;
	
	public DeleteLocalPictureEvent(String id) {
		super();
		this.id = id;
	}

	@Override
	protected void dispatch(GalleryControllerEventHandler handler) {
		handler.deleteLocalPicture(this);
	}

	public String getId() {
		return id;
	}

}
