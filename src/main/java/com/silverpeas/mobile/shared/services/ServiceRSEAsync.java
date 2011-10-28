package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServiceRSEAsync {
	void updateStatus(String status, AsyncCallback<Void> callback);
}
