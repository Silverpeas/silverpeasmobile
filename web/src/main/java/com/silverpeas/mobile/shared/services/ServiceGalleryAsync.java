package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.gallery.AlbumDTO;
import com.silverpeas.mobile.shared.dto.gallery.PhotoDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public interface ServiceGalleryAsync {

	void uploadPicture(String name, String data, String idGallery, String idAlbum, AsyncCallback<Void> callback);

	void getAllGalleries(AsyncCallback<List<ApplicationInstanceDTO>> callback);

	void getAllAlbums(String instanceId, AsyncCallback<List<AlbumDTO>> callback);

	void getAllPictures(String instanceId, String albumId, AsyncCallback<List<PhotoDTO>> callback);

}
