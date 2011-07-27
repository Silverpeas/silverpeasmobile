package com.oosphere.silverpeasmobile.handler;

import javax.servlet.http.HttpServletRequest;

import com.oosphere.silverpeasmobile.bean.WebBeanFactory;
import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.profile.ProfileManager;
import com.silverpeas.socialNetwork.myProfil.control.SocialNetworkService;
import com.stratelia.webactiv.beans.admin.OrganizationController;

public class ProfileHandler extends Handler {

  @Override
  public String getPage(HttpServletRequest request) throws SilverpeasMobileException {
    String page = "profile.jsp";
    String subAction = request.getParameter("subAction");
    if ("profile".equals(subAction)) {
      ProfileManager profileManager = getProfileManager(request);
      page = profile(request, profileManager);
    } else if ("changeStatus".equals(subAction)) {
      ProfileManager profileManager = getProfileManager(request);
      page = changeStatus(request, profileManager);
    } else if ("doChangeStatus".equals(subAction)) {
      ProfileManager profileManager = getProfileManager(request);
      page = doChangeStatus(request, profileManager);
    } else if ("infoThread".equals(subAction)) {
      ProfileManager profileManager = getProfileManager(request);
      page = dashboard(request, profileManager);
    }
    return "profile/" + page;
  }

  private String profile(HttpServletRequest request, ProfileManager profileManager)
      throws SilverpeasMobileException {
    String userId = request.getParameter("userId");
    request.setAttribute("userId", userId);
    return "profile.jsp";
  }

  private String changeStatus(HttpServletRequest request, ProfileManager profileManager)
      throws SilverpeasMobileException {
    String userId = request.getParameter("userId");
    String currentStatus = profileManager.getCurrentStatus(userId);

    request.setAttribute("status", currentStatus);
    request.setAttribute("userId", userId);
    return "changeStatus.jsp";
  }

  private String doChangeStatus(HttpServletRequest request, ProfileManager profileManager)
      throws SilverpeasMobileException {
    String userId = request.getParameter("userId");
    String newStatus = request.getParameter("status");

    String currentStatus = profileManager.setNewStatus(userId, newStatus);

    request.setAttribute("status", currentStatus);
    request.setAttribute("userId", userId);
    return "changeStatus.jsp";
  }

  private String dashboard(HttpServletRequest request, ProfileManager profileManager)
      throws SilverpeasMobileException {
    String userId = request.getParameter("userId");
    
    request.setAttribute("userId", userId);
    return "infoThread.jsp";
  }

  private ProfileManager getProfileManager(HttpServletRequest request)
      throws SilverpeasMobileException {
    WebBeanFactory beanFactory = new WebBeanFactory();
    OrganizationController organizationController = new OrganizationController();
    return new ProfileManager(beanFactory.getAdminBm(), organizationController);
  }

}
