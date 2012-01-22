package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.AlbumDTO;
import com.silverpeas.mobile.shared.dto.ApplicationInstanceDTO;

public interface ServiceGalleryAsync {

	void uploadPicture(String name, String data, AsyncCallback<Void> callback);

	void getAllGalleries(AsyncCallback<List<ApplicationInstanceDTO>> callback);

	void getAllAlbums(String instanceId, AsyncCallback<List<AlbumDTO>> callback);

}
