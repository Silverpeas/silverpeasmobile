package com.silverpeas.mobile.client.apps.gallery.events.controller;


public class SyncPicturesEvent extends AbstractGalleryControllerEvent {
	
	private String idGallery;
	private String idAlbum;
	
	public SyncPicturesEvent(String idGallery, String idAlbum) {
		super();	
		this.idGallery = idGallery;
		this.idAlbum = idAlbum;
	}

	public String getIdGallery() {
		return idGallery;
	}

	public String getIdAlbum() {
		return idAlbum;
	}

	@Override
	protected void dispatch(GalleryControllerEventHandler handler) {
		handler.syncPictures(this);
	}
}
