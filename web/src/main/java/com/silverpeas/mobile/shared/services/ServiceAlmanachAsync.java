package com.silverpeas.mobile.shared.services;

import java.util.Collection;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;

public interface ServiceAlmanachAsync {
	void getAlmanach(String instanceId, AsyncCallback<Collection<EventDetailDTO>> callback);
}
