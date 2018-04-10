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
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;

/**
 * @author: svu
 */
public abstract class RequestCallbackOnlineOrOffline<T> implements RequestCallback {

    private static ApplicationMessages msg = GWT.create(ApplicationMessages.class);
    private Command offlineAction;

    public RequestCallbackOnlineOrOffline(Command offlineAction) {
        this.offlineAction = offlineAction;
    }

    public abstract void attempt();

    @Override
    public void onResponseReceived(Request request, Response response) {
        OfflineHelper.hideOfflineIndicator();
    }

    @Override
    public void onError(Request request, Throwable t) {
       onError(t);
    }

    public void onError(Throwable t) {
        if (t instanceof AuthenticationException) {
            SpMobil.getInstance().loadIds(new Command() {
                @Override
                public void execute() {
                    attempt();
                }
            });
            if (OfflineHelper.needToGoOffine(t)) {
                if (offlineAction != null) offlineAction.execute();
            }
        } else {
            if (OfflineHelper.needToGoOffine(t)) {
                if (offlineAction != null) offlineAction.execute();
            } else {
                EventBus.getInstance().fireEvent(new ErrorEvent(t));
            }
        }
    }
}
