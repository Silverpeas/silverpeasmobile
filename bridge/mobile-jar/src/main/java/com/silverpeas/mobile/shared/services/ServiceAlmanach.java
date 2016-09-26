package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;
import com.silverpeas.mobile.shared.exceptions.AlmanachException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;

@RemoteServiceRelativePath("Almanach")
public interface ServiceAlmanach extends RemoteService{
	List<EventDetailDTO> getAlmanach(String instanceId) throws AlmanachException, AuthenticationException;
}
