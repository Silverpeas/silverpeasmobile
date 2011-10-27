package com.silverpeas.mobile.client.pages.status;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServiceRSEAsync {

	void updateStatus(String status, AsyncCallback<Void> callback);

}
