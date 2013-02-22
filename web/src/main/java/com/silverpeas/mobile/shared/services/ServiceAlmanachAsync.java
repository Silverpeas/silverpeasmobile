package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;

public interface ServiceAlmanachAsync {
	void getAlmanach(String instanceId, AsyncCallback<List<EventDetailDTO>> callback);
}
