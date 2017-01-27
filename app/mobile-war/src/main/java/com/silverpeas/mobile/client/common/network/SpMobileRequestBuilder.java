package com.silverpeas.mobile.client.common.network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.rebind.ConfigurationProvider;

/**
 * @author: svu
 */
public class SpMobileRequestBuilder extends RequestBuilder {
  public final static ConfigurationProvider configuration = GWT.create(ConfigurationProvider.class);
  private int timeout;

  public SpMobileRequestBuilder(Method httpMethod, String url) {
    super(httpMethod, url);
    timeout = Integer.parseInt(configuration.getTimeOutRequest());
    setTimeoutMillis(timeout);
    if (SpMobil.user != null) {
      setHeader("X-Silverpeas-Session", SpMobil.user.getToken());
    }
  }
}
