package com.silverpeas.mobile.client.pages.gallery.controler.event;

import com.silverpeas.mobile.client.persist.GallerySettings;

public class GalleryLoadedSettingsEvent extends AbstractGalleryEvent {

	private GallerySettings settings;	
	
	public GalleryLoadedSettingsEvent(GallerySettings settings) {
		super();
		this.settings = settings;
	}

	@Override
	protected void dispatch(GalleryEventHandler handler) {
		handler.onLoadedSettings(this);
	}

	public GallerySettings getSettings() {
		return settings;
	}
}
