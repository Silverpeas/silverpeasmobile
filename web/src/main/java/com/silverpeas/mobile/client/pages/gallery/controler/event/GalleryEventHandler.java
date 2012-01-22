package com.silverpeas.mobile.client.pages.gallery.controler.event;

import com.google.gwt.event.shared.EventHandler;

public interface GalleryEventHandler extends EventHandler {
	void onLoadedSettings(GalleryLoadedSettingsEvent event);
}