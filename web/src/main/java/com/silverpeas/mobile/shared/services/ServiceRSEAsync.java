package com.silverpeas.mobile.shared.services;

import java.util.Date;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServiceRSEAsync {
	void updateStatus(String status, AsyncCallback<String> callback);
	void getLastStatusService(AsyncCallback<String> callback);
	void getStatus(int indicator, AsyncCallback<Map<Date, String>> callback);
}
