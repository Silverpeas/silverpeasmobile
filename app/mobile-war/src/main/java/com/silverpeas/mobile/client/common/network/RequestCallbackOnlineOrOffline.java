package com.silverpeas.mobile.client.common.network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;

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
