package com.silverpeas.mobile.client.apps.notifications.events.app;

import com.google.gwt.event.shared.EventHandler;

public interface NotificationsAppEventHandler extends EventHandler{
  void sendNotification(SendNotificationEvent event);
}
