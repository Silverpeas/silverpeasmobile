package com.silverpeas.mobile.client.common.network;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.silverpeas.mobile.client.SpMobil;

/**
 * @author: svu
 */
public class SpMobileRpcRequestBuilder extends RpcRequestBuilder {
  private int timeout;

  public SpMobileRpcRequestBuilder() {
    timeout = SpMobileRequestBuilder.TIMEOUT;
  }

  public SpMobileRpcRequestBuilder(int timeout) {
    this.timeout = timeout;
  }

  @Override
  protected RequestBuilder doCreate(String serviceEntryPoint) {
    RequestBuilder builder = super.doCreate(serviceEntryPoint);
    builder.setTimeoutMillis(this.timeout);
    if (SpMobil.getUserToken() != null) {
      builder.setHeader("X-Silverpeas-Session", SpMobil.getUserToken());
    }

    return builder;
  }
}
