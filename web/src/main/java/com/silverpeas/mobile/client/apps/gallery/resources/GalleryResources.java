package com.silverpeas.mobile.client.apps.gallery.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface GalleryResources extends ClientBundle {
	
	@Source("gallery.css")
	GalleryCSS css();
	
	@Source("galleries.png")
	ImageResource galleries();
	
	@Source("albums.png")
	ImageResource albums();
	
	@Source("rPictures.png")
	ImageResource rPictures();
	
	@Source("lPictures.png")
	ImageResource lPictures();
	
	@Source("synchro.png")
	ImageResource synchro();
	
	@Source("camera.png")
	ImageResource camera();
}
