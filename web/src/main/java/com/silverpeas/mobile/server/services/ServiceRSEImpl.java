package com.silverpeas.mobile.server.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.RSEexception;
import com.silverpeas.mobile.shared.services.ServiceRSE;
import com.silverpeas.socialNetwork.model.SocialInformation;
import com.silverpeas.socialNetwork.status.Status;
import com.silverpeas.socialNetwork.status.StatusService;
import com.silverpeas.util.StringUtil;
import com.stratelia.webactiv.beans.admin.UserDetail;

public class ServiceRSEImpl extends AbstractAuthenticateService implements ServiceRSE {

	private static final long serialVersionUID = 1L;
	private int myId;
	GregorianCalendar calendarEnd = new GregorianCalendar();
	GregorianCalendar calendarBegin = new GregorianCalendar(); 
	private List<SocialInformation> listSocialInfo = new ArrayList<SocialInformation>();
	private Map<Date, String> mapRSE = new HashMap<Date, String>();
	
	@Override
	public String updateStatus(String textStatus) throws RSEexception{
		Status status = new Status(myId, new Date(), textStatus);
	    return new StatusService().changeStatusService(status);
	}
	
	public String getLastStatusService() throws RSEexception, AuthenticationException{
		checkUserInSession();
		UserDetail user = getUserInSession();
		myId = Integer.parseInt(user.getId());
	    Status status = new StatusService().getLastStatusService(myId);
	    if (StringUtil.isDefined(status.getDescription())) {
	      return status.getDescription();
	    }
	    return " ";
	}
	
	public Map<Date, String> getAllStatus() throws RSEexception{
		calendarBegin.add(Calendar.MONTH, -1);
		
		com.silverpeas.calendar.Date dBegin = new com.silverpeas.calendar.Date(calendarBegin.getTime());
	    com.silverpeas.calendar.Date dEnd = new com.silverpeas.calendar.Date(calendarEnd.getTime());
	    
		listSocialInfo = new StatusService().getAllStatusService(myId, dBegin, dEnd);
		Iterator<SocialInformation> i = listSocialInfo.iterator();
		while(i.hasNext()){
			SocialInformation SI = i.next();
			mapRSE.put(SI.getDate(),SI.getDescription());
		}
		return mapRSE;
	}
}

