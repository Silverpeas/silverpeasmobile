package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.RSEexception;

@RemoteServiceRelativePath("RSE")
public interface ServiceRSE extends RemoteService {		
	public void updateStatus(String status) throws AuthenticationException, RSEexception;
}
