package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.ApplicationInstanceDTO;

public interface ServiceAdminAsync {
	void getAllSpaces(String spaceType, int level, AsyncCallback<List<ApplicationInstanceDTO>> callback);
}
