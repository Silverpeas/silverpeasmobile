package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.StatusDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.RSEexception;

@RemoteServiceRelativePath("RSE")
public interface ServiceRSE extends RemoteService {
  public String updateStatus(String status) throws RSEexception, AuthenticationException;
  public List<StatusDTO> getStatus(int step) throws RSEexception, AuthenticationException;
  public StatusDTO getStatus() throws RSEexception, AuthenticationException;
}
