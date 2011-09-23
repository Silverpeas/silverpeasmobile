package com.oosphere.silverpeasmobile.handler;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oosphere.silverpeasmobile.CookieManager;
import com.oosphere.silverpeasmobile.bean.WebBeanFactory;
import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.login.LoginManager;
import com.oosphere.silverpeasmobile.servlet.MainController;
import com.oosphere.silverpeasmobile.trace.SilverpeasMobileTrace;
import com.oosphere.silverpeasmobile.utils.StringUtils;
import com.stratelia.silverpeas.authentication.EncryptionFactory;
import com.stratelia.silverpeas.peasCore.MainSessionController;
import com.stratelia.webactiv.beans.admin.Domain;
import com.stratelia.webactiv.beans.admin.OrganizationController;

public class LoginHandler extends Handler {

  @Override
  public String getPage(HttpServletRequest request)
      throws SilverpeasMobileException {
    String page = "login.jsp";
    String subAction = request.getParameter("subAction");
    try {
//      String login = request.getParameter("login");
//      request.setAttribute("login", login);
//      request.setAttribute("login", retreiveLogin(request));
      
      
      
      if ("login".equals(subAction)) {
        LoginManager loginManager = getLoginManager(request);
        page = login(request, loginManager);
      } else if ("home".equals(subAction)) {
        request.setAttribute("userId", request.getParameter("userId"));
        page = "home.jsp";
      } else {
//        request.setAttribute("login", retreiveLoginFromCookie(request));
        
        String storedLogin = retreiveStoredLogin(request);
        String storedPassword = retreiveEncryptedStoredPassword(request);
        String storedDomain = retreiveStoredDomain(request);
        boolean isLogout = StringUtils.isValued(request.getParameter("logout"));
        if(!isLogout && StringUtils.isValued(storedLogin) && StringUtils.isValued(storedPassword) && StringUtils.isValued(storedDomain)){
          return doLogin(request, getLoginManager(request), storedLogin, storedPassword, storedDomain, true);
        } else {
          if(StringUtils.isValued(storedLogin)){
            request.setAttribute("login", storedLogin);
          }
          if(StringUtils.isValued(storedPassword)){
            request.setAttribute("passwordAlreadyStored", "true");
            request.setAttribute("passwordEncrypted", "true");
            request.setAttribute("password", storedPassword);
          }
          populateDomainsInRequest(request);
        }
        
        
        
      }
    } catch (SilverpeasMobileException e) {
      SilverpeasMobileTrace.error(this, "getPage", "EX_GLOBAL_ERROR", e);
      request.setAttribute("error", "technicalError");
      
      populateDomainsInRequest(request);
      
      page = "login.jsp";
    }
    return page;
  }

  private void populateDomainsInRequest(HttpServletRequest request) {
    List<Domain> allDomainsList = getDomains();
    request.setAttribute("domains", allDomainsList);
    
    String domainIdFromUrl = request.getParameter("DomainId");
    if(!StringUtils.isValued(domainIdFromUrl)){
      CookieManager cookieManager = new CookieManager();
      domainIdFromUrl = cookieManager.getStoredDomain(request);
    }
    request.setAttribute("domainIdFromUrl", domainIdFromUrl);
  }

  private List<Domain> getDomains() {
    OrganizationController organizationController = new OrganizationController();
    Domain[] allDomains = organizationController.getAllDomains();
    List<Domain> allDomainsList = Arrays.asList(allDomains);
    return allDomainsList;
  }

  private String login(HttpServletRequest request, LoginManager loginManager)
      throws SilverpeasMobileException {
    
    String login = request.getParameter("login");
    request.setAttribute("login", login);
    String password = request.getParameter("password");
    String domainId = request.getParameter("domainId");
    boolean isPasswordEncrypted = "true".equals(request.getParameter("passwordEncrypted"));
    
    return doLogin(request, loginManager, login, password, domainId, isPasswordEncrypted);
  }

  private String doLogin(HttpServletRequest request, LoginManager loginManager, String login,
      String password, String domainId, boolean isPasswordEncrypted) throws SilverpeasMobileException {
    if (!StringUtils.isValued(login) || !StringUtils.isValued(password)) {
      request.setAttribute("error", "invalidLogin");
      
      populateDomainsInRequest(request);
      
      return "login.jsp";
    }
    
    if(isPasswordEncrypted){
      password = EncryptionFactory.getInstance().getEncryption().decode(password);
    }
    

    String authentificationKey = loginManager.authenticate(login, password, domainId);
    if (authentificationKey != null && !authentificationKey.startsWith("Error")) {
      HttpSession session = request.getSession(true);
      session.setAttribute("authentificationKey", authentificationKey);
      try {
        session.setAttribute(MainSessionController.MAIN_SESSION_CONTROLLER_ATT,
            new MainSessionController(authentificationKey, session.getId()));
      } catch (Exception e) {
        throw new SilverpeasMobileException(this, "login",
            "Error while constructiong MainSessionController object", e);
      }
      
      String userId = loginManager.getUserId(login, domainId);
      request.setAttribute("userId", userId);
      request.setAttribute("authentificationKey", authentificationKey);
      return "welcome.jsp";
    } else {
      request.setAttribute("error",
          ("Error_1".equals(authentificationKey) ? "invalidLogin" : "technicalError"));
      request.setAttribute("login", login);
      
      populateDomainsInRequest(request);
      
      return "login.jsp";
    }
  }

  private String retreiveStoredLogin(HttpServletRequest request){
    String login = "";
    
    CookieManager cookieManager = new CookieManager();
    login = cookieManager.getStoredLogin(request);
    
    return login;
  }
  
  private String retreiveEncryptedStoredPassword(HttpServletRequest request){
    String password = "";
    
    CookieManager cookieManager = new CookieManager();
    password = cookieManager.getEncryptedStoredPassword(request);
    
    return password;
  }
  
  private String retreiveStoredDomain(HttpServletRequest request){
    String domain = "";
    
    CookieManager cookieManager = new CookieManager();
    domain = cookieManager.getStoredDomain(request);
    
    return domain;
  }

  private LoginManager getLoginManager(HttpServletRequest request)
      throws SilverpeasMobileException {
    WebBeanFactory beanFactory = new WebBeanFactory();
    OrganizationController organizationController = new OrganizationController();
    return new LoginManager(beanFactory.getAdminBm(), organizationController);
  }

}
