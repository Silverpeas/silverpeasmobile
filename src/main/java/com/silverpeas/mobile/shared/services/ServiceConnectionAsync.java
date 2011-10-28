package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServiceConnectionAsync {
	void connection(String login, String password, AsyncCallback<Void> callback);
}
