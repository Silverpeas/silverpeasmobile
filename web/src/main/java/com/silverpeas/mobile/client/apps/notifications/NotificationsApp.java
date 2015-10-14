package com.silverpeas.mobile.client.apps.notifications;

import com.google.gwt.user.client.Window;
import com.silverpeas.mobile.client.apps.notifications.events.app.AbstractNotificationsAppEvent;
import com.silverpeas.mobile.client.apps.notifications.events.app.NotificationsAppEventHandler;
import com.silverpeas.mobile.client.apps.notifications.events.app.SendNotificationEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.AllowedUsersAndGroupsLoadedEvent;
import com.silverpeas.mobile.client.apps.notifications.pages.NotificationPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.shared.dto.BaseDTO;

import java.util.List;

/**
 * @author: svu
 */
public class NotificationsApp extends App implements NotificationsAppEventHandler {

    private NotificationPage mainPage = new NotificationPage();

    public NotificationsApp(String contentId, String instanceId, String contentType, String pageTitle, String title) {
        super();
        EventBus.getInstance().addHandler(AbstractNotificationsAppEvent.TYPE, this);
        mainPage.setTitle(title);
        mainPage.setPageTitle(pageTitle);
        //mainPage.setContentInfos(contentId, instanceId, contentType);
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
                //TODO : pass real param
                ServicesLocator.getServiceNotifications().getAllowedUsersAndGroups("1", this);
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
    public void sendNotification(SendNotificationEvent event) {

    }
}
