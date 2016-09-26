package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.SocialInformationDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.DashboardException;

@RemoteServiceRelativePath("Dashboard")
public interface ServiceDashboard extends RemoteService {
	public List<SocialInformationDTO> getAll(int reinitialisationPage, String socialInformationType) throws DashboardException, AuthenticationException;
}
