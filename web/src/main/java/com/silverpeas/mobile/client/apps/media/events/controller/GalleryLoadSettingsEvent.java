package com.silverpeas.mobile.client.apps.media.events.controller;

public class GalleryLoadSettingsEvent extends AbstractGalleryControllerEvent {
	
	public GalleryLoadSettingsEvent() {
		super();
	}

	@Override
	protected void dispatch(GalleryControllerEventHandler handler) {
		handler.loadSettings(this);
	}
}
