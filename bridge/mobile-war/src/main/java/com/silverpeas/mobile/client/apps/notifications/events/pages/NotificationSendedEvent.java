package com.silverpeas.mobile.client.apps.notifications.events.pages;

public class NotificationSendedEvent extends AbstractNotificationPagesEvent {


	public NotificationSendedEvent(){
		super();
	}
	
	@Override
	protected void dispatch(NotificationPagesEventHandler handler) {
		handler.onNotificationSended(this);
	}
}
