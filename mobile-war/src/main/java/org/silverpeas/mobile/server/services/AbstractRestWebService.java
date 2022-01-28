package org.silverpeas.mobile.server.services;

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
}
