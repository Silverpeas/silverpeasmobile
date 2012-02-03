package com.silverpeas.mobile.client.apps.gallery.events.pages;

import java.util.List;

import com.silverpeas.mobile.shared.dto.AlbumDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class GalleryNewInstanceLoadedEvent extends AbstractGalleryPagesEvent {

	private ApplicationInstanceDTO instance;
	private List<AlbumDTO> albums;
	
	public GalleryNewInstanceLoadedEvent(ApplicationInstanceDTO instance, List<AlbumDTO> albums) {
		super();
		this.instance = instance;
		this.albums = albums;
	}

	@Override
	protected void dispatch(GalleryPagesEventHandler handler) {
		handler.onNewGalleryInstanceLoaded(this);
	}

	public ApplicationInstanceDTO getInstance() {
		return instance;
	}

	public List<AlbumDTO> getAlbums() {
		return albums;
	}
}
