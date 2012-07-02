package com.silverpeas.mobile.client.apps.status.events.app.internal;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractStatusEvent extends GwtEvent<StatusEventHandler>{

	public static Type<StatusEventHandler> TYPE = new Type<StatusEventHandler>();
	
	public AbstractStatusEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StatusEventHandler> getAssociatedType() {
		return TYPE;
	}
}
