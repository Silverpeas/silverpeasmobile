package com.silverpeas.mobile.client.apps.status.events.app;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractStatusAppEvent extends GwtEvent<StatusAppEventHandler>{

	public static Type<StatusAppEventHandler> TYPE = new Type<StatusAppEventHandler>();
	
	public AbstractStatusAppEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StatusAppEventHandler> getAssociatedType() {
		return TYPE;
	}
}
