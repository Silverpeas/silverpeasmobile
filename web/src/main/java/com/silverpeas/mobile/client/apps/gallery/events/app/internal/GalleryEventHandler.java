package com.silverpeas.mobile.client.apps.gallery.events.app.internal;

import com.google.gwt.event.shared.EventHandler;

public interface GalleryEventHandler extends EventHandler {
	void onStop(GalleryStopEvent event);
}