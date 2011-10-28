package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;

@RemoteServiceRelativePath("Connection")
public interface ServiceConnection extends RemoteService{
	public void connection(String login, String password) throws AuthenticationException;
}
