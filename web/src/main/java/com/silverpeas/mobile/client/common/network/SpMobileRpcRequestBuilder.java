package com.silverpeas.mobile.client.common.network;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;

/**
 * @author: svu
 */
public class SpMobileRpcRequestBuilder extends RpcRequestBuilder {
  private int timeout = 10000;

  public SpMobileRpcRequestBuilder() {
  }

  public SpMobileRpcRequestBuilder(int timeout) {
    this.timeout = timeout;
  }

  @Override
  protected RequestBuilder doCreate(String serviceEntryPoint) {
    RequestBuilder builder = super.doCreate(serviceEntryPoint);
    builder.setTimeoutMillis(this.timeout);

    return builder;
  }
}
