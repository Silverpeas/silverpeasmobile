package com.silverpeas.mobile.shared.services;

import java.util.Collection;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;

public interface ServiceAlmanachAsync {
<<<<<<< HEAD
	void getAlmanach(String instanceId, AsyncCallback<Collection<EventDetailDTO>> callback);
=======
	void getAllRDV(int month, AsyncCallback<Collection<EventDetailDTO>> callback);
>>>>>>> 09b477a4f719202ea331d7ac57a295f981f9520f
}
