package com.silverpeas.mobile.client.apps.almanach.events.app.internal;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractAlmanachEvent extends GwtEvent<AlmanachEventHandler>{

	public static Type<AlmanachEventHandler> TYPE = new Type<AlmanachEventHandler>();
	
	public AbstractAlmanachEvent(){
		
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AlmanachEventHandler> getAssociatedType() {
		return TYPE;
	}
}
