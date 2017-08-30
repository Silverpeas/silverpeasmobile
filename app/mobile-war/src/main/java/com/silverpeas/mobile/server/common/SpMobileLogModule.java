package com.silverpeas.mobile.server.common;

import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;

public class SpMobileLogModule {

  private static String logModuleName = null;

  public static String getName() {
    if (logModuleName == null) {
      SettingBundle mobileSettings = ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
      logModuleName = mobileSettings.getString("log.module.name", "");
    }
    return logModuleName;
  }
}
