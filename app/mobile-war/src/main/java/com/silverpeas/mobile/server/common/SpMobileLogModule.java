package com.silverpeas.mobile.server.common;

import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;

public class SpMobileLogModule {

  private static String logModuleName;

  static {
    SettingBundle mobileSettings =
        ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
    logModuleName = mobileSettings.getString("", "");
  }

  public static String getName() {
    return logModuleName;
  }
}
