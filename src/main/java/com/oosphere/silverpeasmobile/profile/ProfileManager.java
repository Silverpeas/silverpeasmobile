package com.oosphere.silverpeasmobile.profile;

import com.silverpeas.admin.ejb.AdminBm;
import com.silverpeas.socialNetwork.myProfil.control.SocialNetworkService;
import com.stratelia.webactiv.beans.admin.OrganizationController;

public class ProfileManager {

  private AdminBm adminBm;
  private OrganizationController organizationController;

  public ProfileManager(AdminBm adminBm, OrganizationController organizationController) {
    this.adminBm = adminBm;
    this.organizationController = organizationController;
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
