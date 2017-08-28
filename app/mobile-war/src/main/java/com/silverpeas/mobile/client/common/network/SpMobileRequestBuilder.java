package com.silverpeas.mobile.client.common.network;

import com.google.gwt.http.client.RequestBuilder;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.resources.ResourcesManager;

/**
 * @author: svu
 */
public class SpMobileRequestBuilder extends RequestBuilder {
  public static final int TIMEOUT = Integer.parseInt(ResourcesManager.getParam("ws.timeout"));

  public SpMobileRequestBuilder(Method httpMethod, String url) {
    super(httpMethod, url);
    setTimeoutMillis(TIMEOUT);
    if (SpMobil.user != null) {
      setHeader("X-Silverpeas-Session", SpMobil.user.getToken());
    }
  }
}
