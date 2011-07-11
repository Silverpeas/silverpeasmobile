package com.oosphere.silverpeasmobile.handler;

import javax.servlet.http.HttpServletRequest;

import com.oosphere.silverpeasmobile.bean.WebBeanFactory;
import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.login.LoginManager;
import com.oosphere.silverpeasmobile.trace.SilverpeasMobileTrace;
import com.oosphere.silverpeasmobile.utils.StringUtils;
import com.stratelia.webactiv.beans.admin.OrganizationController;

public class LoginHandler extends Handler {

  @Override
  public String getPage(HttpServletRequest request)
  throws SilverpeasMobileException {
    String page = "login.jsp";
    String subAction = request.getParameter("subAction");
    try {
      if ("login".equals(subAction)) {
        LoginManager loginManager = getLoginManager(request);
        page = login(request, loginManager);
      }
    } catch (SilverpeasMobileException e) {
      SilverpeasMobileTrace.error(this, "getPage", "EX_GLOBAL_ERROR", e);
      request.setAttribute("error", "technicalError");
      page = "login.jsp";
    }
    return page;
  }
  
  private String login(HttpServletRequest request, LoginManager loginManager)
  throws SilverpeasMobileException {
    String login = request.getParameter("login");
    String password = request.getParameter("password");
    if (!StringUtils.isValued(login) || !StringUtils.isValued(password)) {
      request.setAttribute("error", "invalidLogin");
      request.setAttribute("login", login);
      return "login.jsp";
    }
    
    String domainId = request.getParameter("domainId");
    String authentificationKey = loginManager.authenticate(login, password, domainId);
    if (authentificationKey != null && !authentificationKey.startsWith("Error")) {
      String userId = loginManager.getUserId(login, domainId);
      request.setAttribute("userId", userId);
      return "welcome.jsp";
    } else {
      request.setAttribute("error",
        ("Error_1".equals(authentificationKey) ? "invalidLogin" : "technicalError"));
      request.setAttribute("login", login);
      return "login.jsp";
    }
  }
  
  private LoginManager getLoginManager(HttpServletRequest request)
  throws SilverpeasMobileException {
    WebBeanFactory beanFactory = new WebBeanFactory();
    OrganizationController organizationController = new OrganizationController();
    return new LoginManager(beanFactory.getAdminBm(), organizationController);
  }

}
