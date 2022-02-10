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

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.StatusCodeException;
import org.fusesource.restygwt.client.FailedResponseException;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.resources.ApplicationMessages;

/**
 * @author: svu
 */
public class OfflineHelper {

    private static ApplicationMessages msg = GWT.create(ApplicationMessages.class);
    private static boolean offLine;

    public static boolean needToGoOffine (Throwable reason) {
        if (reason instanceof FailedResponseException) {
          if (((FailedResponseException) reason).getStatusCode() == 0) {
            SpMobil.getMainPage().showOfflineIndicator();
            offLine = true;
            return offLine;
          }
        }

        if (reason instanceof StatusCodeException) {
            if (((StatusCodeException) reason).getStatusCode() == 0) {
                SpMobil.getMainPage().showOfflineIndicator();
                offLine = true;
                return offLine;
            }
        }
        if (reason instanceof RequestTimeoutException) {
            SpMobil.getMainPage().showOfflineIndicator();
            offLine = true;
            return offLine;
        }
        SpMobil.getMainPage().hideOfflineIndicator();
        offLine = false;
        return offLine;
    }

    public static void hideOfflineIndicator() {
        SpMobil.getMainPage().hideOfflineIndicator();
    }

    public static boolean isOffLine() {
        return offLine;
    }
}
