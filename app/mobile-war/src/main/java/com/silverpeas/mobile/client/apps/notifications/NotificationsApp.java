package com.silverpeas.mobile.client.apps.notifications;

import com.google.gwt.core.client.GWT;
import com.silverpeas.mobile.client.apps.notifications.events.app.AbstractNotificationsAppEvent;
import com.silverpeas.mobile.client.apps.notifications.events.app.NotificationsAppEventHandler;
import com.silverpeas.mobile.client.apps.notifications.events.app.SendNotificationEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.AllowedUsersAndGroupsLoadedEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.NotificationSendedEvent;
import com.silverpeas.mobile.client.apps.notifications.pages.NotificationPage;
import com.silverpeas.mobile.client.apps.notifications.resources.NotificationsMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

import java.util.List;

/**
 * @author: svu
 */
public class NotificationsApp extends App implements NotificationsAppEventHandler {

    private NotificationPage mainPage = new NotificationPage();
    private String instanceId, contentId, contentType;
    private ApplicationMessages globalMsg = null;
    private NotificationsMessages msg = null;

    public NotificationsApp(String instanceId, String contentId, String contentType, String title, String pageTitle) {
        super();
        globalMsg = GWT.create(ApplicationMessages.class);
        msg = GWT.create(NotificationsMessages.class);
        this.instanceId = instanceId;
        this.contentId = contentId;
        this.contentType = contentType;
        EventBus.getInstance().addHandler(AbstractNotificationsAppEvent.TYPE, this);
        mainPage.setTitle(title);
        mainPage.setPageTitle(msg.notifyContent());
    }

    public void start(){
        setMainPage(mainPage);
        super.start();
        loadUsersAndGroups();
    }

    @Override
    public void stop() {
        EventBus.getInstance().removeHandler(AbstractNotificationsAppEvent.TYPE, this);
        super.stop();
    }

    public void loadUsersAndGroups() {

        AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<List<BaseDTO>>(null) {
            @Override
            public void attempt() {
                ServicesLocator.getServiceNotifications().getAllowedUsersAndGroups(instanceId, contentId, this);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                if (OfflineHelper.isOffLine()) {
                    Notification.alert(globalMsg.needToBeOnline());
                }
            }

            @Override
            public void onSuccess(List<BaseDTO> result) {
                super.onSuccess(result);
                // No storage in local storage

                // Notify view
                EventBus.getInstance().fireEvent(new AllowedUsersAndGroupsLoadedEvent(result));
            }
        };
        action.attempt();
    }

    @Override
    public void sendNotification(final SendNotificationEvent event) {
        AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<Void>() {
            @Override
            public void attempt() {
                NotificationDTO n = event.getNotification();
                n.setContentId(contentId);
                n.setContentType(contentType);
                n.setInstanceId(instanceId);
                ServicesLocator.getServiceNotifications().send(n, event.getReceivers(), event.getSubject(), this);
            }

            @Override
            public void onSuccess(Void result) {
                super.onSuccess(result);
                // No storage in local storage

                // Notify view
                EventBus.getInstance().fireEvent(new NotificationSendedEvent());
            }
        };
        action.attempt();
    }
}
