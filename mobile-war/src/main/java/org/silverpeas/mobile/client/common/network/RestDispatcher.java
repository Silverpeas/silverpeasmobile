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

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Window;
import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.SpMobil;

/**
 * @author svu
 */
public class RestDispatcher implements Dispatcher {

  @Override
  public Request send(final Method method, final RequestBuilder builder)
      throws RequestException {
    builder.setTimeoutMillis(SpMobileRequestBuilder.TIMEOUT);
    builder.setHeader("Authorization", "Bearer " + SpMobil.getUser().getToken());
    if (SpMobil.getUser().getSessionKey() != null) {
      builder.setHeader("X-STKN", SpMobil.getUser().getSessionKey()); // mandatory for PUT,POST,DELETE
    } else {
      GWT.log("User session key is null !");
    }
    return builder.send();
  }
}
