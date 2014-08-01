package com.silverpeas.mobile.client.apps.media.events.pages.navigation;

import com.google.gwt.event.shared.EventHandler;

public interface MediaNavigationPagesEventHandler extends EventHandler {
	void onLoadedAlbums(MediaItemsLoadedEvent event);
	void onMediaItemClicked(MediaItemClickEvent event);
}