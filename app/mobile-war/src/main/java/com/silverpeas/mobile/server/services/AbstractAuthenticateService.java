package com.silverpeas.mobile.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException.AuthenticationError;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.web.mvc.controller.MainSessionController;

@SuppressWarnings("serial")
public abstract class AbstractAuthenticateService extends RemoteServiceServlet {
  public static final String USER_ATTRIBUT_NAME = "user";
  public static final String USERKEY_ATTRIBUT_NAME = "key";
  public static final String MAINSESSIONCONTROLLER_ATTRIBUT_NAME = "SilverSessionController";

  protected void setUserInSession(UserDetail user) {
    getThreadLocalRequest().getSession().setAttribute(USER_ATTRIBUT_NAME, user);
  }

  protected void setUserkeyInSession(String key) {
    getThreadLocalRequest().getSession().setAttribute(USERKEY_ATTRIBUT_NAME, key);
  }

  protected UserDetail getUserInSession() {
    return (UserDetail) getThreadLocalRequest().getSession().getAttribute(USER_ATTRIBUT_NAME);
  }

  protected String getUserKeyInSession() {
    return (String) getThreadLocalRequest().getSession().getAttribute(USERKEY_ATTRIBUT_NAME);
  }

  protected void checkUserInSession() throws AuthenticationException {
    if (getThreadLocalRequest().getSession().getAttribute(USER_ATTRIBUT_NAME) == null) {
      throw new AuthenticationException(AuthenticationError.NotAuthenticate);
    }
  }


  protected MainSessionController getMainSessionController() throws Exception {
    MainSessionController mainSessionController = (MainSessionController) getThreadLocalRequest().getSession().getAttribute(MAINSESSIONCONTROLLER_ATTRIBUT_NAME);
    return mainSessionController;
  }
}
