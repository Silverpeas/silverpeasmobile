package com.oosphere.silverpeasmobile.profile;

import com.silverpeas.socialNetwork.myProfil.control.SocialNetworkService;

public class ProfileManager {


  public ProfileManager() {
  }

  public void updateStatus(String status) {

  }

  public String getCurrentStatus(String userId) {
    SocialNetworkService socialNetworkService = new SocialNetworkService(userId);
    return socialNetworkService.getLastStatusService();
  }
  
  public String setNewStatus(String userId, String newStatus) {
    SocialNetworkService socialNetworkService = new SocialNetworkService(userId);
    return socialNetworkService.changeStatusService(newStatus);
  }

}
