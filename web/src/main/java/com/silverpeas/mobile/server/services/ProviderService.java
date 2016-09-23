package com.silverpeas.mobile.server.services;

import com.silverpeas.calendar.Date;
import com.silverpeas.jcrutil.BasicDaoFactory;
import org.silverpeas.core.silvertrace.SilverTrace;
import org.silverpeas.core.socialnetwork.model.SocialInformation;
import org.silverpeas.core.socialnetwork.model.SocialInformationType;
import org.silverpeas.core.socialnetwork.provider.ProviderSwitchInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
              "root.MSG_GEN_ENTER_METHOD" + ex);
    }
    return new ArrayList<SocialInformation>();
  }
}
