package com.silverpeas.mobile.client.apps.gallery.persistances;

import com.gwtmobile.persistence.client.Persistable;

public interface GallerySettings extends Persistable {
	public String getSelectedAlbumId();
	public void setSelectedAlbumId(String albumId);
	public String getSelectedGalleryId();
	public void setSelectedGalleryId(String galleryId);
	public String getSelectedGalleryLabel();
	public void setSelectedGalleryLabel(String galleryLabel);
}
