package com.silverpeas.mobile.server.services;

import java.util.ArrayList;
import java.util.List;

import com.silverpeas.calendar.Date;
import com.silverpeas.jcrutil.BasicDaoFactory;
import com.silverpeas.socialnetwork.model.SocialInformation;
import com.silverpeas.socialnetwork.model.SocialInformationType;
import com.silverpeas.socialnetwork.provider.ProviderSwitchInterface;
import com.stratelia.silverpeas.silvertrace.SilverTrace;

public class ProviderService {
	private ProviderSwitchInterface switchInterface;

	  public ProviderService() {
	    switchInterface = (ProviderSwitchInterface) BasicDaoFactory.getBean("providerSwitch");
	  }
	  
	  public List<SocialInformation> getSocialInformationsListOfMyContact(SocialInformationType socialInformationType, String myId,
		      List<String> myContactIds, Date begin, Date end) {
		    try {
		      return switchInterface.getSocialInformationsListOfMyContacts(socialInformationType, myId,
		          myContactIds, begin, end);
		    } catch (Exception ex) {
		      SilverTrace.info("socialNetwork", "ProviderService.getSocialInformationsListOfMyContact",
		        "root.MSG_GEN_ENTER_METHOD" +ex);
		    }
		    return new ArrayList<SocialInformation>();
	 }
}
