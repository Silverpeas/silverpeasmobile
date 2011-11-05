package com.silverpeas.mobile.server.services;

import java.util.Calendar;

import com.silverpeas.mobile.server.dao.StatusDao;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.RSEexception;
import com.silverpeas.mobile.shared.services.ServiceRSE;
import com.silverpeas.socialNetwork.status.Status;
import com.stratelia.webactiv.beans.admin.UserDetail;

public class ServiceRSEImpl extends AbstractAuthenticateService implements ServiceRSE {

	private static final long serialVersionUID = 1L;

	// TODO : injecter avec Spring
	private StatusDao statusDao = new StatusDao();	

	public void updateStatus(String status) throws AuthenticationException, RSEexception {
		checkUserInSession();
		UserDetail user = getUserInSession();
		
		Status stat = new Status(Integer.parseInt(user.getId()), Calendar.getInstance().getTime(), status);
		try {
			statusDao.changeStatus(stat);
		} catch (Exception e) {
			throw new RSEexception(e.getMessage());
		} 
	}
}
