package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.contact.ContactFilters;

public interface ServiceContactAsync {
	void getContacts(ContactFilters filter, AsyncCallback<List<DetailUserDTO>> callback);
	void getContactDetail(String id, AsyncCallback<DetailUserDTO> callback);
}
