package com.silverpeas.mobile.client.apps.gallery.events.app.internal;

public class GalleryStopEvent extends AbstractGalleryEvent {
	
	public GalleryStopEvent() {
		super();
	}

	@Override
	protected void dispatch(GalleryEventHandler handler) {
		handler.onStop(this);
	}
}
