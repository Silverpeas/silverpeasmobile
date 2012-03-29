package com.silverpeas.mobile.client.apps.gallery.events.controller;


public class LoadRemotePreviewPictureEvent extends AbstractGalleryControllerEvent {
	
	private String galleryId;
	private String photoId;
	
	public LoadRemotePreviewPictureEvent(String galleryId, String photoId) {
		super();
		this.galleryId = galleryId;
		this.photoId = photoId;
	}

	@Override
	protected void dispatch(GalleryControllerEventHandler handler) {
		handler.loadRemotePreviewPicture(this);
	}

	public String getGalleryId() {
		return galleryId;
	}

	public String getPhotoId() {
		return photoId;
	}
}
