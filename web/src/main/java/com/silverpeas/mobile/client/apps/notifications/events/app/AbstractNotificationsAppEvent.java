package com.silverpeas.mobile.client.apps.notifications.events.app;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractNotificationsAppEvent extends GwtEvent<NotificationsAppEventHandler>{

	public static Type<NotificationsAppEventHandler> TYPE = new Type<NotificationsAppEventHandler>();
	
	public AbstractNotificationsAppEvent(){
	}
	
	@Override
	public Type<NotificationsAppEventHandler> getAssociatedType() {
		return TYPE;
	}
}
