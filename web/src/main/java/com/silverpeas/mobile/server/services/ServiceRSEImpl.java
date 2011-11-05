package com.silverpeas.mobile.server.services;

import java.util.Calendar;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.silverpeas.mobile.server.dao.StatusDao;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException.AuthenticationError;
import com.silverpeas.mobile.shared.exceptions.RSEexception;
import com.silverpeas.mobile.shared.services.ServiceRSE;
import com.silverpeas.socialNetwork.status.Status;
import com.stratelia.webactiv.beans.admin.UserDetail;

public class ServiceRSEImpl extends RemoteServiceServlet implements ServiceRSE {

	private static final long serialVersionUID = 1L;

	// TODO : injecter avec Spring
	private StatusDao statusDao = new StatusDao();	

	public void updateStatus(String status) throws AuthenticationException, RSEexception {
		//TODO : rendre générique
		UserDetail user = (UserDetail) getThreadLocalRequest().getSession().getAttribute("user");
		if (user == null) throw new AuthenticationException(AuthenticationError.NotAuthenticate);
		
		Status stat = new Status(Integer.parseInt(user.getId()), Calendar.getInstance().getTime(), status);
		try {
			statusDao.changeStatus(stat);
		} catch (Exception e) {
			throw new RSEexception(e.getMessage());
		} 
	}
}
