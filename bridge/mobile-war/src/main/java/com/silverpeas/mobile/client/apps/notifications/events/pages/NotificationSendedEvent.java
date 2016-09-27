package com.silverpeas.mobile.client.apps.notifications.events.pages;

import com.silverpeas.mobile.shared.dto.BaseDTO;

import java.util.List;

public class NotificationSendedEvent extends AbstractNotificationPagesEvent {


	public NotificationSendedEvent(){
		super();
	}
	
	@Override
	protected void dispatch(NotificationPagesEventHandler handler) {
		handler.onNotificationSended(this);
	}
}
