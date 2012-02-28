package com.silverpeas.mobile.client.apps.gallery.events.controller;

import com.google.gwt.event.shared.EventHandler;

public interface GalleryControllerEventHandler extends EventHandler {
	void loadSettings(GalleryLoadSettingsEvent event);
	void saveSettings(GallerySaveSettingsEvent event);
	void deleteLocalPicture(DeleteLocalPictureEvent event);
}