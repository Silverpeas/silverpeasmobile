package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.StatusDTO;

public interface ServiceRSEAsync {
	void updateStatus(String status, AsyncCallback<String> callback);
	void getStatus(int step, AsyncCallback<List<StatusDTO>> callback);
}
