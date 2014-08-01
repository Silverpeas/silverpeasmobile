package com.silverpeas.mobile.client.apps.media.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface GalleryPagesEventHandler extends EventHandler {
	void onLoadedSettings(GalleryLoadedSettingsEvent event);
	void onNewGalleryInstanceLoaded(GalleryNewInstanceLoadedEvent event);
	void onLocalPicturesLoaded(GalleryLocalPicturesLoadedEvent event);
	void onStartingUpload(GalleryStartingUploadEvent event);
	void onPictureUploaded(GalleryPictureUploadedEvent event);
	void onEndUpload(GalleryEndUploadEvent event);
}