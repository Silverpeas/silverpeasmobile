package com.silverpeas.mobile.client.apps.gallery.events.controller;

import com.google.gwt.event.shared.EventHandler;

public interface GalleryControllerEventHandler extends EventHandler {
	void loadSettings(GalleryLoadSettingsEvent event);
	void saveSettings(GallerySaveSettingsEvent event);
	void deleteLocalPicture(DeleteLocalPictureEvent event);
	void loadLocalPictures(LoadLocalPicturesEvent event);	
	void syncPictures(SyncPicturesEvent event);
	void takePicture(TakePictureEvent event);
	void loadRemotePictures(RemotePicturesLoadEvent event);
	void loadRemotePreviewPicture(LoadRemotePreviewPictureEvent event);
}