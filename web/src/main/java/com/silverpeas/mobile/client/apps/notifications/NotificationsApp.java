package com.silverpeas.mobile.client.apps.notifications;

import com.google.gwt.user.client.Command;
import com.silverpeas.mobile.client.apps.comments.events.app.AbstractCommentsAppEvent;
import com.silverpeas.mobile.client.apps.comments.events.app.AddCommentEvent;
import com.silverpeas.mobile.client.apps.comments.events.app.CommentsAppEventHandler;
import com.silverpeas.mobile.client.apps.comments.events.app.CommentsLoadEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentAddedEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentsLoadedEvent;
import com.silverpeas.mobile.client.apps.comments.pages.CommentsPage;
import com.silverpeas.mobile.client.apps.notifications.events.app.AbstractNotificationsAppEvent;
import com.silverpeas.mobile.client.apps.notifications.events.app.NotificationsAppEventHandler;
import com.silverpeas.mobile.client.apps.notifications.events.app.SendNotificationEvent;
import com.silverpeas.mobile.client.apps.notifications.pages.NotificationPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;

import java.util.ArrayList;
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
    }

    @Override
    public void stop() {
        EventBus.getInstance().removeHandler(AbstractNotificationsAppEvent.TYPE, this);
        super.stop();
    }

    @Override
    public void sendNotification(SendNotificationEvent event) {

    }
}
