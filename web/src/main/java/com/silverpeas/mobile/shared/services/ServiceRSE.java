package com.silverpeas.mobile.shared.services;

import java.util.Date;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.exceptions.RSEexception;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;

@RemoteServiceRelativePath("RSE")
public interface ServiceRSE extends RemoteService {		
	public String updateStatus(String status) throws RSEexception;
	public String getLastStatusService() throws RSEexception, AuthenticationException;
	public Map<Date, String> getAllStatus() throws RSEexception;
}
