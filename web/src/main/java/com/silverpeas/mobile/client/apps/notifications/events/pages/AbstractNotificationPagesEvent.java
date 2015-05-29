package com.silverpeas.mobile.client.apps.notifications.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractNotificationPagesEvent extends GwtEvent<NotificationPagesEventHandler> {

	public static Type<NotificationPagesEventHandler> TYPE = new Type<NotificationPagesEventHandler>();
	
	public AbstractNotificationPagesEvent() {
	}
	
	@Override
	public Type<NotificationPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
