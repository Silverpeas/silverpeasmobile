package com.silverpeas.mobile.client.apps.media.events.pages.remote;

import com.google.gwt.event.shared.EventHandler;

public interface RemotePicturesPageEventHandler extends EventHandler {
	void onPicturesLoaded(RemotePictureLoadedEvent event);
}