/*
 * Copyright (C) 2000 - 2018 Silverpeas
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.common.network;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.googlecode.gwt.crypto.bouncycastle.util.encoders.Base64;
import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Method;

/**
 * @author svu
 */
public class RestAuthenticationDispatcher implements Dispatcher {

  private String login, password, domainId;

  public RestAuthenticationDispatcher(final String login, final String password,
      final String domainId) {
    this.login = login;
    this.password = password;
    this.domainId = domainId;
  }

  @Override
  public Request send(final Method method, final RequestBuilder builder)
      throws RequestException {
    String credentials = login + "@domain" + domainId + ":" + password;
    byte[] credentialsEncoded = Base64.encode(credentials.getBytes());
    builder.setTimeoutMillis(SpMobileRequestBuilder.TIMEOUT);
    builder.setHeader("Authorization", "Basic " + convertByteArrayToString(credentialsEncoded));

    return builder.send();
  }

  private static String convertByteArrayToString(byte[] byteArray) {
    String s = "";
    for (int i = 0; i < byteArray.length; i++) {
      s += (char) (byteArray[i]);
    }
    return s;
  }
}
