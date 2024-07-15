/*
 * Copyright (C) 2000 - 2024 Silverpeas
 *
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
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

import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.rpc.StatusCodeException;
import org.fusesource.restygwt.client.FailedResponseException;
import org.silverpeas.mobile.client.SpMobil;

/**
 * @author: svu
 */
public class NetworkHelper {

    private static NetworkHelper instance = null;
    private String connectionType = null;

    public static NetworkHelper getInstance() {
        if (instance == null) {
            instance = new NetworkHelper();
        }
        return instance;
    }

    public NetworkHelper() {
      watchConnectionState();
      watchConnectionType(this);
    }

    public static String getContext() {
        return "/silverpeas/spmobile/";
    }

    private static native void watchConnectionType(NetworkHelper i) /*-{
        var connection = window.navigator.connection || window.navigator.mozConnection || null;
        if (connection != null) {
            if ('metered' in connection) {
                return;
            } else {
                connection.addEventListener('typechange', function (event) {
                    i.@org.silverpeas.mobile.client.common.network.NetworkHelper::connectionType = connection.type;
                });
                connection.dispatchEvent(new Event('typechange'));
            }
        } else {
            return;
        }
    }-*/;

    private static native void watchConnectionState() /*-{
      $wnd.addEventListener("offline", function () {
        @org.silverpeas.mobile.client.common.network.NetworkHelper::updateConnexionIndicator(*)(true);
      }, false);
      $wnd.addEventListener("online", function () {
        @org.silverpeas.mobile.client.common.network.NetworkHelper::updateConnexionIndicator(*)(false);
      }, false);
    }-*/;

    private static native boolean isNetworkDetectionAvailable() /*-{
        var connection = window.navigator.connection || window.navigator.mozConnection || null;
        return (connection == null);
    }-*/;

  public static native boolean isOnline() /*-{
    var connection = window.navigator.onLine;
    return connection;
  }-*/;

    public static void updateConnexionIndicator() {
      if (isOnline()) {
        SpMobil.getMainPage().hideOfflineIndicator();
      } else {
        SpMobil.getMainPage().showOfflineIndicator();
      }
    }

  public static boolean needToGoOffine (Throwable reason) {
      if (reason instanceof FailedResponseException) {
        if (((FailedResponseException) reason).getStatusCode() == 0) {
          SpMobil.getMainPage().showOfflineIndicator();
          return true;
        }
      }

      if (reason instanceof StatusCodeException) {
          if (((StatusCodeException) reason).getStatusCode() == 0) {
              SpMobil.getMainPage().showOfflineIndicator();
              return true;
          }
      }
      if (reason instanceof RequestTimeoutException) {
          SpMobil.getMainPage().showOfflineIndicator();
          return true;
      }
      SpMobil.getMainPage().hideOfflineIndicator();
      return false;
  }

  public static void hideOfflineIndicator() {
      SpMobil.getMainPage().hideOfflineIndicator();
  }

  public String getConnectionType() {
        return connectionType;
    }

    public boolean isOnWifi() {
        if (connectionType != null) {
            return connectionType.equalsIgnoreCase(ConnectionType.wifi.toString());
        } else {
            return false;
        }
    }
}
