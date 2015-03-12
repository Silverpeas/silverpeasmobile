package com.silverpeas.mobile.client.common.navigation;

import com.google.gwt.user.client.Window;

/**
 * @author: svu
 */
public class UrlUtils {

  public static String getLocation() {
    String url = Window.Location.getProtocol() + "//" + Window.Location.getHost();
    url += "/spmobile/";
    return url;
  }
}
