package com.silverpeas.mobile.client.common.network;

import com.google.gwt.http.client.RequestBuilder;
import com.silverpeas.mobile.client.SpMobil;

/**
 * @author: svu
 */
public class SpMobileRequestBuilder extends RequestBuilder {
  public static final int TIMEOUT = 10000;

  public SpMobileRequestBuilder(Method httpMethod, String url) {
    super(httpMethod, url);
    setTimeoutMillis(TIMEOUT);
    if (SpMobil.user != null) {
      setHeader("X-Silverpeas-Session", SpMobil.user.getToken());
    }
  }
}
