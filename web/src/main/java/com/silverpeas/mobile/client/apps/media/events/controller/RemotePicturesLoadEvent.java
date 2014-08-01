package com.silverpeas.mobile.client.apps.media.events.controller;

public class RemotePicturesLoadEvent extends AbstractGalleryControllerEvent {
	
	private String galleryId;
	private String albumId;
	
	public RemotePicturesLoadEvent(String galleryId, String albumId) {
		super();
		this.galleryId = galleryId;
		this.albumId = albumId;
	}

	@Override
	protected void dispatch(GalleryControllerEventHandler handler) {
		handler.loadRemotePictures(this);
	}

	public String getGalleryId() {
		return galleryId;
	}

	public String getAlbumId() {
		return albumId;
	}
}
