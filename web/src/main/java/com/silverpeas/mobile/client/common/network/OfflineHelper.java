package com.silverpeas.mobile.client.common.network;

import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.rpc.StatusCodeException;

/**
 * @author: svu
 */
public class OfflineHelper {

  public static boolean needToGoOffine (Throwable reason) {
    if (reason instanceof StatusCodeException) {
      if (((StatusCodeException) reason).getStatusCode() == 0) {
        return true;
      }
    }
    if (reason instanceof RequestTimeoutException) {
      return true;
    }
    return false;
  }
}
