package com.silverpeas.mobile.client.apps.notifications.events.app;

import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

import java.util.List;

public class SendNotificationEvent extends AbstractNotificationsAppEvent {

    private NotificationDTO notification;
    private List<BaseDTO> receivers;
    private String subject;

    public List<BaseDTO> getReceivers() {
        return receivers;
    }

    public SendNotificationEvent(NotificationDTO notification, List<BaseDTO> receivers, String subject){
        super();
        this.notification = notification;
        this.receivers = receivers;
        this.subject = subject;
    }

    @Override
    protected void dispatch(NotificationsAppEventHandler handler) {
        handler.sendNotification(this);
    }

    public NotificationDTO getNotification() {
        return notification;
    }

    public String getSubject() {
        return subject;
    }
}
