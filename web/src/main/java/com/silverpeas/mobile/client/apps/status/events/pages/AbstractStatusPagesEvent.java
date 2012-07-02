package com.silverpeas.mobile.client.apps.status.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractStatusPagesEvent extends GwtEvent<StatusPagesEventHandler>{
	
	public static Type<StatusPagesEventHandler> TYPE = new Type<StatusPagesEventHandler>();

	public AbstractStatusPagesEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StatusPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
