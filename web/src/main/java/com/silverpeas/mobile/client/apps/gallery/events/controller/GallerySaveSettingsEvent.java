package com.silverpeas.mobile.client.apps.gallery.events.controller;

import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class GallerySaveSettingsEvent extends AbstractGalleryControllerEvent {
	
	private ApplicationInstanceDTO gallery;
	private String albumId;
	
	public GallerySaveSettingsEvent(ApplicationInstanceDTO gallery, String albumId) {
		super();
		this.gallery = gallery;
		this.albumId = albumId;
	}

	@Override
	protected void dispatch(GalleryControllerEventHandler handler) {
		handler.saveSettings(this);
	}

	public ApplicationInstanceDTO getGallery() {
		return gallery;
	}

	public String getAlbumId() {
		return albumId;
	}
}
