package com.silverpeas.mobile.client.apps.media.events.pages.local;

import com.google.gwt.event.shared.EventHandler;

public interface LocalPicturesPageEventHandler extends EventHandler {
	void onDeletedLocalPicture(DeletedLocalPictureEvent event);
}