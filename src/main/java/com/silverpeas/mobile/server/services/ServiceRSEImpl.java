package com.silverpeas.mobile.server.services;

import java.sql.Connection;
import java.util.Date;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.silverpeas.mobile.shared.services.ServiceRSE;
import com.silverpeas.mobile.shared.services.Status;
import com.silverpeas.mobile.shared.services.StatusDao;
import com.stratelia.webactiv.util.DBUtil;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.UtilException;
import com.sun.star.sdbc.SQLException;

public class ServiceRSEImpl extends RemoteServiceServlet implements ServiceRSE {

	private static final long serialVersionUID = 1L;
	
	private ServiceRSE ServiceRSE;
	private StatusDao statusDao;
	private String myID = "2";
	
	public void StatusService() {
	    statusDao = new StatusDao();
	  }
	
	private Connection getConnection() throws UtilException, SQLException {
	    return DBUtil.makeConnection(JNDINames.DATABASE_DATASOURCE);
	  }

	public void updateStatus(String status) {
		// TODO Auto-generated method stub
		
		Status Status = new Status(Integer.parseInt(myID), new Date(), status);
		
		Connection connection = null;
	    int id = -1;
	    try {
	      connection = getConnection();
	      id = statusDao.changeStatus(connection, Status);
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
