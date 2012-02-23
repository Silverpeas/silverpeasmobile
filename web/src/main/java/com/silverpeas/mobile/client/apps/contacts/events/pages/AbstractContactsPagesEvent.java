package com.silverpeas.mobile.client.apps.contacts.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractContactsPagesEvent extends GwtEvent<ContactsPagesEventHandler>{

	public static Type<ContactsPagesEventHandler> TYPE = new Type<ContactsPagesEventHandler>();
	
	public AbstractContactsPagesEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ContactsPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
