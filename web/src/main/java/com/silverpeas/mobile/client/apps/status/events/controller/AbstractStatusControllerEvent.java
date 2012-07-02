package com.silverpeas.mobile.client.apps.status.events.controller;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractStatusControllerEvent extends GwtEvent<StatusControllerEventHandler>{

	public static Type<StatusControllerEventHandler> TYPE = new Type<StatusControllerEventHandler>();
	
	public AbstractStatusControllerEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StatusControllerEventHandler> getAssociatedType() {
		return TYPE;
	}
}
