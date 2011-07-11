package com.oosphere.silverpeasmobile.login;

import java.rmi.RemoteException;

import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.silverpeas.admin.ejb.AdminBm;
import com.stratelia.silverpeas.authentication.LoginPasswordAuthentication;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.UserDetail;

public class LoginManager {

  private AdminBm adminBm;
  private OrganizationController organizationController;
  
  public LoginManager(AdminBm adminBm, OrganizationController organizationController) {
    this.adminBm = adminBm;
    this.organizationController = organizationController;
  }

  public String authenticate(String login, String password, String domainId) {
    LoginPasswordAuthentication lpAuth = new LoginPasswordAuthentication();
    return lpAuth.authenticate(login, password, domainId, null);
  }
  
  public String getUserId(String login, String domainId)
  throws SilverpeasMobileException {
    try {
      return adminBm.getUserIdByLoginAndDomain(login, domainId);
    } catch (RemoteException e) {
      throw new SilverpeasMobileException(this, "getUserId", "EX_GET_USER_ID_FAILED",
        "login=" + login + " ; domainId=" + domainId, e);
    }
  }
  
  public String getUserName(String userId) {
    String name = null;
    UserDetail user = getUserDetail(userId);
    if (user != null) {
      name = user.getDisplayedName();
    }
    return (name != null ? name.trim() : "");
  }
  
  private UserDetail getUserDetail(String userId) {
    return organizationController.getUserDetail(userId);
  }
  
}
