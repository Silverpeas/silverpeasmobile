package com.silverpeas.mobile.server.services;

import org.silverpeas.core.date.Date;
import org.silverpeas.core.socialnetwork.model.SocialInformation;
import org.silverpeas.core.socialnetwork.model.SocialInformationType;
import org.silverpeas.core.socialnetwork.provider.ProviderSwitchInterface;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.core.util.logging.SilverLogger;

import java.util.ArrayList;
import java.util.List;

public class ProviderService {
  private ProviderSwitchInterface switchInterface;

  public ProviderService() {
    switchInterface = ServiceProvider.getService(ProviderSwitchInterface.class);
  }

  public List<SocialInformation> getSocialInformationsListOfMyContact(SocialInformationType socialInformationType, String myId,
      List<String> myContactIds, Date begin, Date end) {
    try {
      return switchInterface.getSocialInformationsListOfMyContacts(socialInformationType, myId, myContactIds, begin, end);
    } catch (Exception ex) {
        SilverLogger.getLogger("socialNetwork").error("ProviderService.getSocialInformationsListOfMyContact",ex);
    }
    return new ArrayList<SocialInformation>();
  }
}
