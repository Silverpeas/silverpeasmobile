package com.silverpeas.mobile.client.common.network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;

/**
 * @author: svu
 */
public class OfflineHelper {

    private static ApplicationMessages msg = GWT.create(ApplicationMessages.class);
    private static boolean offLine;

    public static boolean needToGoOffine (Throwable reason) {
        if (reason instanceof StatusCodeException) {
            if (((StatusCodeException) reason).getStatusCode() == 0) {
                SpMobil.mainPage.showOfflineIndicator();
                offLine = true;
                return offLine;
            }
        }
        if (reason instanceof RequestTimeoutException) {
            SpMobil.mainPage.showOfflineIndicator();
            offLine = true;
            return offLine;
        }
        SpMobil.mainPage.hideOfflineIndicator();
        offLine = false;
        return offLine;
    }

    public static void hideOfflineIndicator() {
        SpMobil.mainPage.hideOfflineIndicator();
    }

    public static boolean isOffLine() {
        return offLine;
    }
}
