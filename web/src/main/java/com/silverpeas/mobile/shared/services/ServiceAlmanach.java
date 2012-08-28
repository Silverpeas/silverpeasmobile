package com.silverpeas.mobile.shared.services;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;
import com.silverpeas.mobile.shared.exceptions.AlmanachException;

@RemoteServiceRelativePath("Almanach")
public interface ServiceAlmanach extends RemoteService{
	Collection<EventDetailDTO> getAlmanach(String instanceId) throws AlmanachException;
}
