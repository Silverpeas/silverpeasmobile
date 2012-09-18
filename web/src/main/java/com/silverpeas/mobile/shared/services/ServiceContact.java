package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.contact.ContactFilters;
import com.silverpeas.mobile.shared.exceptions.ContactException;

@RemoteServiceRelativePath("Contact")
public interface ServiceContact extends RemoteService{
	List<DetailUserDTO> getContacts(ContactFilters filter) throws ContactException;
	DetailUserDTO getContactDetail(String id) throws ContactException;
}
