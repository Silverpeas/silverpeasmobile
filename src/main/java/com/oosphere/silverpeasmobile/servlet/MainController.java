package com.oosphere.silverpeasmobile.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oosphere.silverpeasmobile.CookieManager;
import com.oosphere.silverpeasmobile.handler.ContactHandler;
import com.oosphere.silverpeasmobile.handler.Handler;
import com.oosphere.silverpeasmobile.handler.KmeliaHandler;
import com.oosphere.silverpeasmobile.handler.LoginHandler;
import com.oosphere.silverpeasmobile.handler.ProfileHandler;
import com.oosphere.silverpeasmobile.handler.TaskHandler;
import com.oosphere.silverpeasmobile.trace.SilverpeasMobileTrace;
import com.oosphere.silverpeasmobile.utils.StringUtils;
import com.silverpeas.SilverpeasServiceProvider;
import com.silverpeas.util.StringUtil;
import com.stratelia.silverpeas.authentication.EncryptionFactory;

public class MainController extends HttpServlet {

  private static final int THREE_YEARS_AS_SECONDS = 3 * 365 * 24 * 60 * 60;

  private static final long serialVersionUID = 6933108376306279880L;
  
  public static final String SILVERPEAS_MOBILE_COOKIE = "SilverpeasMobileLogin";

  private Map<String, Handler> handlers = new HashMap<String, Handler>();
  private String defaultLanguage = "en";

  public MainController() {
    handlers.put("login", new LoginHandler());
    handlers.put("kmelia", new KmeliaHandler());
    handlers.put("profile", new ProfileHandler());
    handlers.put("contact", new ContactHandler());
    handlers.put("task", new TaskHandler());
  }

  public void init(ServletConfig config) throws ServletException {
    super.init();
    try {
      String language = config.getInitParameter("language");
      SilverpeasMobileTrace.init(language);
      defaultLanguage = language;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServletException("MainController init error : " + e.getMessage());
    }
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    executeRequest(request, response);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    executeRequest(request, response);
  }

  /**
   * @param request
   * @param response
   * @throws IOException
   * @throws ServletException
   */
  private void executeRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String page = null;

    setLanguage(request);
    
    manageLoginCookie(request, response);

    String action = request.getParameter("action");
    if (!StringUtils.isValued(action)) {
      action = "login";
    }
    Handler handler = handlers.get(action);
    if (handler != null) {
      try {
        page = handler.getPage(request);
        
        if(isLogout(request)){
          removeStoredPassword(response);
        }
      } catch (Exception e) {
        SilverpeasMobileTrace.error(this, "executeRequest", "EX_GLOBAL_ERROR",
            "action = " + action, e);
        request.setAttribute("exception", e);
        page = "error.jsp";
        if("login".equals(action)){
          removeStoredPassword(response);
        }
      }
    } else {
      SilverpeasMobileTrace.error(this, "executeRequest", "EX_GLOBAL_ERROR", "action = " + action);
      page = "error.jsp";
    }
    request.getRequestDispatcher("jsp/" + page).forward(request, response);
  }

  private boolean isLogout(HttpServletRequest request) {
    return StringUtils.isValued(request.getParameter("logout"));
  }

  private void removeStoredPassword(HttpServletResponse response) {
    CookieManager loginCookieManager = new CookieManager();
    loginCookieManager.removeStoredPassword(response);
  }
  
  private void manageLoginCookie(HttpServletRequest request, HttpServletResponse response) {
    CookieManager loginCookieManager = new CookieManager();
    
    String login = request.getParameter("login");
    if(StringUtils.isValued(login)){
      loginCookieManager.storeLogin(response, login);
    }
    
    String storePassword = request.getParameter("storePassword");
    String password = request.getParameter("password");
    if(StringUtils.isValued(storePassword) && StringUtils.isValued(password)){
      boolean isPasswordEncrypted = "true".equals(request.getParameter("passwordEncrypted"));
      loginCookieManager.storePassword(response, CookieManager.PASSWORD_MUST_BE_STORED, password, isPasswordEncrypted);
    }
    
    String domain = request.getParameter("domainId");
    if(StringUtils.isValued(domain)){
      loginCookieManager.storeDomain(response, domain);
    }
  }

//  private void storeLoginInCookie(String login, HttpServletResponse response) {
//    Cookie cookie = new Cookie(SILVERPEAS_MOBILE_COOKIE, login);
//    cookie.setMaxAge(THREE_YEARS_AS_SECONDS);
//    response.addCookie(cookie);
//  }

  private void setLanguage(HttpServletRequest request) {
    String lang = request.getParameter("lang");
    if (!StringUtil.isDefined(lang)) {
      try {
        String userId = request.getParameter("userId");
        if (StringUtil.isDefined(userId)) {
          lang = SilverpeasServiceProvider.getPersonalizationService().getUserSettings(userId)
              .getLanguage();
        }
      } catch (Exception e) {
        // do nothing
      }

      if (!StringUtil.isDefined(lang)) {
        lang = defaultLanguage;
      }
    }
    request.setAttribute("lang", lang);
  }
  

  

}