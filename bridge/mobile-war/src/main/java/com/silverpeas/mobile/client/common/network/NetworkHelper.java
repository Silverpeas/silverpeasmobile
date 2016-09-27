package com.silverpeas.mobile.client.common.network;

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
        watchConnectionType(this);
    }

    private static native void watchConnectionType(NetworkHelper i) /*-{
        var connection = window.navigator.connection || window.navigator.mozConnection || null;
        if (connection != null) {
            if ('metered' in connection) {
                return;
            } else {
                connection.addEventListener('typechange', function (event) {
                    i.@com.silverpeas.mobile.client.common.network.NetworkHelper::connectionType = connection.type;
                });
                connection.dispatchEvent(new Event('typechange'));
            }
        } else {
            return;
        }
    }-*/;

    private static native boolean isNetworkDetectionAvailable() /*-{
        var connection = window.navigator.connection || window.navigator.mozConnection || null;
        return (connection == null);
    }-*/;

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
