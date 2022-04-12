package org.silverpeas.mobile.server.services;

import org.silverpeas.core.SilverpeasException;
import org.silverpeas.core.security.authentication.AuthenticationCredential;
import org.silverpeas.core.security.authentication.AuthenticationService;
import org.silverpeas.core.security.authentication.AuthenticationServiceProvider;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.web.mvc.controller.MainSessionController;
import org.silverpeas.core.web.rs.RESTWebService;

/**
 * @author svu
 */
public abstract class AbstractRestWebService extends RESTWebService {

  public static final String MAINSESSIONCONTROLLER_ATTRIBUT_NAME = "SilverSessionController";

  protected static SettingBundle getSettings() {
    return ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
  }

  protected MainSessionController getMainSessionController() throws Exception {
    return (MainSessionController) getHttpRequest().getSession()
        .getAttribute(MAINSESSIONCONTROLLER_ATTRIBUT_NAME);
  }

  protected void setMainsessioncontroller(String login, String password, String domainId)
      throws SilverpeasException {
    AuthenticationService authService = AuthenticationServiceProvider.getService();
    AuthenticationCredential credential = AuthenticationCredential.newWithAsLogin(login);
    String key =
        authService.authenticate(credential.withAsPassword(password).withAsDomainId(domainId));
    MainSessionController mainSessionController =
        new MainSessionController(key, getHttpRequest().getSession());
  }
}
