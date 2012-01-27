package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.exceptions.AdminException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;

@RemoteServiceRelativePath("Admin")
public interface ServiceAdmin extends RemoteService{
	public List<ApplicationInstanceDTO> getAllSpaces(String spaceType, int level) throws AdminException, AuthenticationException;
}
