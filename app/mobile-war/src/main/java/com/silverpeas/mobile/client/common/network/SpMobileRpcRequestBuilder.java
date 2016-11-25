package com.silverpeas.mobile.client.common.network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.silverpeas.mobile.client.rebind.ConfigurationProvider;

/**
 * @author: svu
 */
public class SpMobileRpcRequestBuilder extends RpcRequestBuilder {
  public final static ConfigurationProvider configuration = GWT.create(ConfigurationProvider.class);
  private int timeout;

  public SpMobileRpcRequestBuilder() {
    timeout = Integer.parseInt(configuration.getTimeOutRequest());
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
