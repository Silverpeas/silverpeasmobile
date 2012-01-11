package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServiceGalleryAsync {

	void uploadPicture(String name, String data, AsyncCallback<Void> callback);

}
