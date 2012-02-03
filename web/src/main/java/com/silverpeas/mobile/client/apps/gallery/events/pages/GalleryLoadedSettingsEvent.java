package com.silverpeas.mobile.client.apps.gallery.events.pages;

import java.util.List;

import com.silverpeas.mobile.client.apps.gallery.persistances.GallerySettings;
import com.silverpeas.mobile.shared.dto.AlbumDTO;

public class GalleryLoadedSettingsEvent extends AbstractGalleryPagesEvent {

	private GallerySettings settings;
	private List<AlbumDTO> albums;
	
	public GalleryLoadedSettingsEvent(GallerySettings settings, List<AlbumDTO> albums) {
		super();
		this.settings = settings;
		this.albums = albums;
	}

	@Override
	protected void dispatch(GalleryPagesEventHandler handler) {
		handler.onLoadedSettings(this);
	}

	public GallerySettings getSettings() {
		return settings;
	}

	public List<AlbumDTO> getAlbums() {
		return albums;
	}
}
