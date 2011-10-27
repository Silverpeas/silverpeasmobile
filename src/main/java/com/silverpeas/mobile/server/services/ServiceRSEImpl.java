package com.silverpeas.mobile.server.services;

import java.sql.Connection;
import java.util.Date;
import java.sql.SQLException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.silverpeas.mobile.client.pages.status.ServiceRSE;
import com.silverpeas.mobile.client.pages.status.Status;
import com.silverpeas.mobile.client.pages.status.StatusDao;
import com.stratelia.webactiv.util.DBUtil;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.UtilException;


public class ServiceRSEImpl extends RemoteServiceServlet implements ServiceRSE {

	private static final long serialVersionUID = 1L;
	
	private ServiceRSE ServiceRSE;
	private StatusDao statusDao;
	private String myId;

	  public ServiceRSEImpl(String myId) {
	    this.myId = myId;
	  }
	
	public void StatusService() {
	    statusDao = new StatusDao();
	  }
	
	private Connection getConnection() throws UtilException, SQLException {
	    return DBUtil.makeConnection(JNDINames.DATABASE_DATASOURCE);
	  }

	public void updateStatus(String status) {
		// TODO Auto-generated method stub
		
		Status Status = new Status(Integer.parseInt(myId), new Date(), status);
		
		Connection connection = null;
	    int id = -1;
	    try {
	      try {
			connection = getConnection();
		} catch (UtilException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      try {
			id = statusDao.changeStatus(connection, Status);
		} catch (UtilException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      if (id >= 0) {
	        //return Status.getDescription();
	      }
	    } finally {
	      DBUtil.close(connection);
	    }
	}
	
	public void setServiceRSE(ServiceRSE ServiceRSE) {
        this.ServiceRSE = ServiceRSE;
    }
}
