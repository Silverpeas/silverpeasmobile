package com.silverpeas.mobile.client.apps.notifications.events.app;

public class SendNotificationEvent extends AbstractNotificationsAppEvent {

    private String contentId, instanceId, contentType, message;

    public SendNotificationEvent(String contentId, String instanceId, String contentType, String message){
        super();
        this.contentId = contentId;
        this.instanceId = instanceId;
        this.contentType = contentType;
        this.message = message;
    }

    @Override
    protected void dispatch(NotificationsAppEventHandler handler) {
        handler.sendNotification(this);
    }

    public String getContentId(){
        return contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public String getMessage() {
        return message;
    }

    public String getInstanceId() {
        return instanceId;
    }
}
