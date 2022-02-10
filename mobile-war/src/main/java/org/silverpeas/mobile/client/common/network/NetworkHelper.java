/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

import org.silverpeas.mobile.client.SpMobil;

/**
 * @author: svu
 */
public class NetworkHelper {

    private static NetworkHelper instance = null;
    private String connectionType = null;
    private boolean offline = false;

    public static NetworkHelper getInstance() {
        if (instance == null) {
            instance = new NetworkHelper();
        }
        return instance;
    }

    public NetworkHelper() {
      watchConnectionState(this);
      watchConnectionType(this);
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

    private static native void watchConnectionState(NetworkHelper i) /*-{
      $wnd.addEventListener("offline", function () {
        i.@org.silverpeas.mobile.client.common.network.NetworkHelper::updateConnexionIndicator(*)(true);
      }, false);
      $wnd.addEventListener("online", function () {
        i.@org.silverpeas.mobile.client.common.network.NetworkHelper::updateConnexionIndicator(*)(false);
      }, false);
    }-*/;

    private static native boolean isNetworkDetectionAvailable() /*-{
        var connection = window.navigator.connection || window.navigator.mozConnection || null;
        return (connection == null);
    }-*/;

    private void updateConnexionIndicator(boolean offline) {
      if (offline) {
        SpMobil.getMainPage().showOfflineIndicator();
        this.offline = true;
      } else {
        SpMobil.getMainPage().hideOfflineIndicator();
        this.offline = false;
      }
    }

    public boolean isOffline() {
      return offline;
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
