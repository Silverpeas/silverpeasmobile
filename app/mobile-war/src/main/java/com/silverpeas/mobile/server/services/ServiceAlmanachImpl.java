package com.silverpeas.mobile.server.services;

import com.silverpeas.mobile.shared.dto.EventDetailDTO;
import com.silverpeas.mobile.shared.exceptions.AlmanachException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.services.ServiceAlmanach;

import java.util.ArrayList;
import java.util.List;

public class ServiceAlmanachImpl extends AbstractAuthenticateService implements ServiceAlmanach {

  private static final long serialVersionUID = 1L;

  public List<EventDetailDTO> getAlmanach(String instanceId) throws AlmanachException, AuthenticationException {
    checkUserInSession();
    List<EventDetailDTO> listEventDetailDTO = new ArrayList<>();
    //TODO
    return listEventDetailDTO;
  }
}