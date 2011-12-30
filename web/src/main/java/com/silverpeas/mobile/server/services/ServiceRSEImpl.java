package com.silverpeas.mobile.server.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.silverpeas.mobile.server.dao.StatusDao;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.RSEexception;
import com.silverpeas.mobile.shared.services.ServiceRSE;
import com.silverpeas.socialNetwork.status.Status;

import com.silverpeas.socialNetwork.status.StatusService;
import com.silverpeas.util.StringUtil;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.util.DBUtil;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.UtilException;

public class ServiceRSEImpl extends AbstractAuthenticateService implements
		ServiceRSE {

	private static final long serialVersionUID = 1L;
	private int myId;
	private StatusDao statusDao = new StatusDao();

	@Override
	public String updateStatus(String textStatus) throws RSEexception {
		Status status = new Status(myId, new Date(), textStatus);
		int id = DBUtil.getNextId("sb_sn_status", "id");
		statusDao.addLastStatus(status, id+1);
		return new StatusService().changeStatusService(status);
	}

	public String getLastStatusService() throws RSEexception,
			AuthenticationException {
		checkUserInSession();
		UserDetail user = getUserInSession();
		myId = Integer.parseInt(user.getId());
		Status status = new StatusService().getLastStatusService(myId);
		if (StringUtil.isDefined(status.getDescription())) {
			return status.getDescription();
		}
		return " ";
	}

	public Map<Date, String> getStatus(int indicator) throws RSEexception {
		Connection connection = null;
		try {
			connection = getConnection();
		    return statusDao.getAllStatus(connection, myId, indicator);
		} catch (Exception ex) {
		    SilverTrace.error("Silverpeas.Bus.SocialNetwork.Status", "StatusService.getAllStatus", "", ex);
		} finally {
			DBUtil.close(connection);
		}
		return new HashMap<Date, String>();	
	}
	
	private Connection getConnection() throws UtilException, SQLException {
	    return DBUtil.makeConnection(JNDINames.DATABASE_DATASOURCE);
	}
	
	/*
	 * public void getAllStatus() throws RSEexception{
	 * calendarBegin.add(Calendar.MONTH, -1);
	 * 
	 * com.silverpeas.calendar.Date dBegin = new
	 * com.silverpeas.calendar.Date(calendarBegin.getTime());
	 * com.silverpeas.calendar.Date dEnd = new
	 * com.silverpeas.calendar.Date(calendarEnd.getTime());
	 * 
	 * List<SocialInformation> listSocialInfo = new
	 * ArrayList<SocialInformation>(); listSocialInfo = new
	 * StatusService().getAllStatusService(myId, dBegin, dEnd);
	 * Iterator<SocialInformation> i = listSocialInfo.iterator(); Map<Date,
	 * String> mapRSE = new HashMap<Date, String>(); while(i.hasNext()){
	 * SocialInformation SI = i.next();
	 * mapRSE.put(SI.getDate(),SI.getDescription()); }
	 * 
	 * map = new TreeMap<Date, String>(mapRSE); }
	 */

	/*
	 * public Map<Date, String> More(int moreCount) throws RSEexception{
	 * Map<Date, String> tempMap = new HashMap<Date, String>(); int i=1;
	 * 
	 * if(moreCount==0){ Iterator<Entry<Date, String>> entries =
	 * map.entrySet().iterator(); while (entries.hasNext()) { Entry<Date,
	 * String> thisEntry = (Entry<Date, String>) entries.next();
	 * if(i>map.size()-5){ tempMap.put((Date)thisEntry.getKey(),
	 * (String)thisEntry.getValue()); } i++; } TreeMap<Date, String> finalMap =
	 * new TreeMap<Date, String>(tempMap); return finalMap; } else{
	 * Iterator<Entry<Date, String>> entries = map.entrySet().iterator(); while
	 * (entries.hasNext()) { Entry<Date, String> thisEntry = (Entry<Date,
	 * String>) entries.next(); if(i<=map.size()-moreCount*5 &&
	 * i>map.size()-(moreCount*5+5)){ tempMap.put((Date)thisEntry.getKey(),
	 * (String)thisEntry.getValue()); } i++; } TreeMap<Date, String> finalMap =
	 * new TreeMap<Date, String>(tempMap); return finalMap; } }
	 */
}
