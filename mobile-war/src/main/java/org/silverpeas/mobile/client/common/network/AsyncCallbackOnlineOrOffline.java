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
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;

/**
 * @author: svu
 */
public abstract class AsyncCallbackOnlineOrOffline<T> implements AsyncCallback<T> {

    private static ApplicationMessages msg = GWT.create(ApplicationMessages.class);
    private Command offlineAction;

    public AsyncCallbackOnlineOrOffline(Command offlineAction) {
        this.offlineAction = offlineAction;
    }

    public abstract void attempt();

    @Override
    public void onFailure(Throwable t) {
        Notification.activityStop();
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

    @Override
    public void onSuccess(T result) {
      Notification.activityStop();
      OfflineHelper.hideOfflineIndicator();
    }
}
