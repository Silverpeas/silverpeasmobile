package com.silverpeas.mobile.client.apps.gallery.resources;

import com.google.gwt.i18n.client.Messages;

public interface GalleryMessages extends Messages {
	String title();
	String currentGallery();
	String takePicture();
	String synchroPictures();
	String browseRemotePictures();
	String browseLocalPictures();
	
	String localPicture_empty();
	String localPicture_delete();
	String localPicture_title();
	String localPicture_deleteConfirmation();
	String localPicture_uploading();
	String localPicture();
	String fullScreen();
}
