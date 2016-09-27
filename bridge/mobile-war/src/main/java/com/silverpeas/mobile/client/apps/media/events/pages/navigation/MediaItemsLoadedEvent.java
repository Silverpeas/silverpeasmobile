package com.silverpeas.mobile.client.apps.media.events.pages.navigation;

import com.silverpeas.mobile.shared.dto.BaseDTO;

import java.util.List;

public class MediaItemsLoadedEvent extends AbstractMediaNavigationPagesEvent {

	private List<BaseDTO> albumsAndMedias;

	public MediaItemsLoadedEvent(List<BaseDTO> albumsAndMedias) {
		super();
		this.albumsAndMedias = albumsAndMedias;
	}

	@Override
	protected void dispatch(MediaNavigationPagesEventHandler handler) {
		handler.onLoadedAlbums(this);
	}

	public List<BaseDTO> getAlbumsAndMedias() {
		return albumsAndMedias;
	}
}
