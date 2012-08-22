package com.silverpeas.mobile.shared.services;

import java.util.Collection;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;

public interface ServiceAlmanachAsync {
	void getAllRDV(int month, AsyncCallback<Collection<EventDetailDTO>> callback);
}
