package com.silverpeas.mobile.server.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.silverpeas.jcrutil.BasicDaoFactory;
import com.silverpeas.mobile.shared.dto.SocialInformation;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.DashboardException;
import com.silverpeas.mobile.shared.services.ServiceDashboard;
import com.silverpeas.socialNetwork.model.SocialInformationType;
import com.silverpeas.socialNetwork.provider.ProviderSwitchInterface;
import com.silverpeas.socialNetwork.relationShip.RelationShipService;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.util.exception.SilverpeasException;

public class ServiceDashboardImpl extends AbstractAuthenticateService implements ServiceDashboard{

	private static final long serialVersionUID = 1L;
	private ProviderSwitchInterface switchInterface;
	private String myId;
	private GregorianCalendar begin = new GregorianCalendar();
	private GregorianCalendar end = new GregorianCalendar();

	@Override
	public Map<Date, List<SocialInformation>> getALL() throws DashboardException, AuthenticationException, SilverpeasException {
		begin.add(Calendar.MONTH, -1);
		com.silverpeas.calendar.Date dBegin = new com.silverpeas.calendar.Date(begin.getTime());
	    com.silverpeas.calendar.Date dEnd = new com.silverpeas.calendar.Date(end.getTime());
	        
	    List<String> myContactIds = getMyContactsIds();
	    
	    List<com.silverpeas.socialNetwork.model.SocialInformation> socialInformationsFull =
	        getSwitchInterface().getSocialInformationsListOfMyContacts(SocialInformationType.ALL, myId,
	            myContactIds, dEnd, dBegin);
	    
	    if (SocialInformationType.ALL.equals(SocialInformationType.ALL)) {
	      Collections.sort(socialInformationsFull);
	    }
	    
	    return processResults(socialInformationsFull);
	}
	
	public List<String> getMyContactsIds() throws AuthenticationException {
	    try {
	    	checkUserInSession();
			UserDetail user = getUserInSession();
			myId = user.getId();
	      return new RelationShipService().getMyContactsIds(Integer.parseInt(myId));
	    } catch (SQLException ex) {
	      SilverTrace.error("socialNetworkService", "SocialNetworkService.getMyContactsIds", "", ex);
	    }
	    return new ArrayList<String>();
	}
	
	public Map<Date, List<SocialInformation>> processResults(List<com.silverpeas.socialNetwork.model.SocialInformation> socialInformationFull){
		return null;
	}
	
	public ProviderSwitchInterface getSwitchInterface(){
		return switchInterface = (ProviderSwitchInterface) BasicDaoFactory.getBean("providerSwitch");
	}

}
