package com.silverpeas.mobile.shared.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.SocialInformation;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.DashboardException;
import com.stratelia.webactiv.util.exception.SilverpeasException;

@RemoteServiceRelativePath("Dashboard")
public interface ServiceDashboard extends RemoteService {
	public Map<Date, List<SocialInformation>> getALL() throws DashboardException, AuthenticationException, SilverpeasException;
}
