package com.silverpeas.mobile.client.apps.gallery.events.pages.local;

import com.google.gwt.event.shared.EventHandler;

public interface LocalPicturesPageEventHandler extends EventHandler {
	void onDeletedLocalPicture(DeletedLocalPictureEvent event);
}