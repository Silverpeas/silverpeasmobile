package com.silverpeas.mobile.client.apps.gallery.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface GalleryPagesEventHandler extends EventHandler {
	void onLoadedSettings(GalleryLoadedSettingsEvent event);
	void onNewGalleryInstanceLoaded(GalleryNewInstanceLoadedEvent event);
}