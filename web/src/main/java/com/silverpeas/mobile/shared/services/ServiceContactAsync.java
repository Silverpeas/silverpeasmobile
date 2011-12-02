package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public interface ServiceContactAsync {
	void getAllContact(AsyncCallback<Void> callback);
	void getContactsByLetter(String letter, AsyncCallback<List<DetailUserDTO>> callback);
}
