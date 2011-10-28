package com.silverpeas.mobile.server.services;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.silverpeas.mobile.server.dao.StatusDao;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.services.ServiceRSE;
import com.stratelia.webactiv.util.DBUtil;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.UtilException;

public class ServiceRSEImpl extends RemoteServiceServlet implements ServiceRSE {

	private static final long serialVersionUID = 1L;

	private StatusDao statusDao = new StatusDao();
	private String myId;

	private Connection getConnection() throws UtilException, SQLException {
		return DBUtil.makeConnection(JNDINames.DATABASE_DATASOURCE);
	}

	public void updateStatus(String status) throws AuthenticationException {
		getThreadLocalRequest().getSession().setAttribute(0, login);
		
		/*Connection connection = null;
	    int id = -1;
	    try {
	      connection = getConnection();
	      id = statusDao.changeStatus(connection, status);
	      if (id >= 0) {
	        return status.getDescription();
	      }
	    } catch (Exception ex) {
	      SilverTrace
	          .error("Silverpeas.Bus.SocialNetwork.Status", "StatusService.changeStatus", "", ex);
	    } finally {
	      DBUtil.close(connection);
	    }*/
		System.out.println("updated");
	}
}
