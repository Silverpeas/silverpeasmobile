package com.silverpeas.mobile.client.common.network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.tasks.events.pages.TasksLoadedEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.TaskDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.realityforge.gwt.appcache.client.ApplicationCache;

import java.util.List;

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
        if (t instanceof AuthenticationException) {
            SpMobil.getInstance().loadIds(new Command() {
                @Override
                public void execute() {
                    attempt();
                }
            }, true);
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
        OfflineHelper.hideOfflineIndicator();
    }
}
