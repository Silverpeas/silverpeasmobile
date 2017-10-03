package com.silverpeas.mobile.client.common.navigation;

import com.google.gwt.user.client.Window;

/**
 * @author: svu
 */
public class UrlUtils {

  public static String getServicesLocation() {
    String url = Window.Location.getProtocol() + "//" + Window.Location.getHost();
    url += "/silverpeas/services/spmobile/";
    return url;
  }

  public static String getUploadLocation() {
    String url = Window.Location.getProtocol() + "//" + Window.Location.getHost();
    url += "/silverpeas/services/spmobile/";
    return url;
  }
}
