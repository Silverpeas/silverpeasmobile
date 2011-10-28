package com.silverpeas.mobile.server.services;

import java.sql.Connection;
import java.sql.SQLException;

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
		// TODO Auto-generated method stub

		/*
		 * Status Status = new Status(Integer.parseInt(myId), new Date(),
		 * status);
		 * 
		 * Connection connection = null; int id = -1; try { try { connection =
		 * getConnection(); } catch (UtilException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } catch (SQLException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } try { id =
		 * statusDao.changeStatus(connection, Status); } catch (UtilException e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } if (id >= 0) { //return
		 * Status.getDescription(); } } finally { DBUtil.close(connection); }
		 */
		System.out.println("updated");
	}
}
