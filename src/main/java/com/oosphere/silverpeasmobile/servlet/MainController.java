package com.oosphere.silverpeasmobile.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oosphere.silverpeasmobile.handler.Handler;
import com.oosphere.silverpeasmobile.handler.KmeliaHandler;
import com.oosphere.silverpeasmobile.handler.LoginHandler;
import com.oosphere.silverpeasmobile.handler.ProfileHandler;
import com.oosphere.silverpeasmobile.trace.SilverpeasMobileTrace;
import com.oosphere.silverpeasmobile.utils.StringUtils;
import com.silverpeas.SilverpeasServiceProvider;
import com.silverpeas.util.StringUtil;

public class MainController extends HttpServlet {

  private static final long serialVersionUID = 6933108376306279880L;

  private Map<String, Handler> handlers = new HashMap<String, Handler>();
  private String defaultLanguage = "en";

  public MainController() {
    handlers.put("login", new LoginHandler());
    handlers.put("kmelia", new KmeliaHandler());
    handlers.put("profile", new ProfileHandler());
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

    String action = request.getParameter("action");
    if (!StringUtils.isValued(action)) {
      action = "login";
    }
    Handler handler = handlers.get(action);
    if (handler != null) {
      try {
        page = handler.getPage(request);
      } catch (Exception e) {
        SilverpeasMobileTrace.error(this, "executeRequest", "EX_GLOBAL_ERROR",
            "action = " + action, e);
        request.setAttribute("exception", e);
        page = "error.jsp";
      }
    } else {
      SilverpeasMobileTrace.error(this, "executeRequest", "EX_GLOBAL_ERROR", "action = " + action);
      page = "error.jsp";
    }
    request.getRequestDispatcher("jsp/" + page).forward(request, response);
  }

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