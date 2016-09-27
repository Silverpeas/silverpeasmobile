package com.silverpeas.mobile.client.common.network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;

/**
 * @author: svu
 */
public abstract class AsyncCallbackOnlineOnly<T> implements AsyncCallback<T> {

    private static ApplicationMessages msg = GWT.create(ApplicationMessages.class);

    public abstract void attempt();

    @Override
    public void onFailure(Throwable t) {
        if (t instanceof AuthenticationException) {
            SpMobil.getInstance().loadIds(new Command() {
                @Override
                public void execute() {
                    attempt();
                }
            }, true);
            if (OfflineHelper.needToGoOffine(t)) {
                Notification.alert(msg.needToBeOnline());
            }
        } else {
            if (OfflineHelper.needToGoOffine(t)) {
                Notification.alert(msg.needToBeOnline());
            } else {
                EventBus.getInstance().fireEvent(new ErrorEvent(t));
            }
        }
    }

    @Override
    public void onSuccess(T result) {
        OfflineHelper.hideOfflineIndicator();
    }
}
