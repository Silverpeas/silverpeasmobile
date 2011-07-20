package com.oosphere.silverpeasmobile.profile;

import com.silverpeas.admin.ejb.AdminBm;
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

  public String getLastStatus(int userid) {
    return "This Is The Current Status";
  }

}
