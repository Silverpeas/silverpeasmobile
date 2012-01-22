package com.silverpeas.mobile.client.persist;

import com.gwtmobile.persistence.client.Persistable;

public interface GallerySettings extends Persistable {
	public String getSelectedAlbumId();
	public void setSelectedAlbumId(String albumId);
	public String getSelectedGalleryId();
	public void setSelectedGalleryId(String galleryId);
}
