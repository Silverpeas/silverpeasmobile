package com.silverpeas.mobile.client.apps.media.events.pages.remote.viewer;

import com.google.gwt.event.shared.EventHandler;

public interface PicturesViewerPageEventHandler extends EventHandler {
	void onPictureLoaded(PictureViewLoadedEvent event);
}