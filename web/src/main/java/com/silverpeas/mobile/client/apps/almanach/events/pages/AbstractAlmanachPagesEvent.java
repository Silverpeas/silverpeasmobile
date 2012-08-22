package com.silverpeas.mobile.client.apps.almanach.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractAlmanachPagesEvent extends GwtEvent<AlmanachPagesEventHandler>{
	
	public static Type<AlmanachPagesEventHandler> TYPE = new Type<AlmanachPagesEventHandler>();
	
	public AbstractAlmanachPagesEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AlmanachPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
