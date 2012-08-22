package com.silverpeas.mobile.client.apps.almanach.events.controller;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractAlmanachControllerEvent extends GwtEvent<AlmanachControllerEventHandler>{

	public static Type<AlmanachControllerEventHandler> TYPE = new Type<AlmanachControllerEventHandler>();
	
	public AbstractAlmanachControllerEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AlmanachControllerEventHandler> getAssociatedType() {
		return TYPE;
	}
}
