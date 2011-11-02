package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServiceContactAsync {
	void ContactList(String id,AsyncCallback<Void> callback);
}
