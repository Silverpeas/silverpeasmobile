package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.exceptions.ContactException;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;

@RemoteServiceRelativePath("Contact")
public interface ServiceContact extends RemoteService {
  List<DetailUserDTO> getContacts(String type, String filter, int pageSize, int startIndex) throws ContactException, AuthenticationException;
  boolean hasContacts() throws ContactException, AuthenticationException;
}
