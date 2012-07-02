package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.DomainDTO;


public interface ServiceConnectionAsync {

	void login(String login, String password, String domainId,
			AsyncCallback<Void> callback);

	void getDomains(AsyncCallback<List<DomainDTO>> callback);
	
}
