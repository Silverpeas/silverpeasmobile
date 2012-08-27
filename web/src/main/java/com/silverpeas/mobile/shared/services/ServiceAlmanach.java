package com.silverpeas.mobile.shared.services;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;
import com.silverpeas.mobile.shared.exceptions.AlmanachException;

@RemoteServiceRelativePath("Almanach")
public interface ServiceAlmanach extends RemoteService{
<<<<<<< HEAD
	Collection<EventDetailDTO> getAlmanach(String instanceId) throws AlmanachException;
=======
	Collection<EventDetailDTO> getAllRDV(int month) throws AlmanachException;
>>>>>>> 09b477a4f719202ea331d7ac57a295f981f9520f
}
