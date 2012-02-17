package com.silverpeas.mobile.client.apps.contacts.events.app.internal;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractContactEvent extends GwtEvent<ContactsEventHandler>{

	public static Type<ContactsEventHandler> TYPE = new Type<ContactsEventHandler>();
	
	public AbstractContactEvent(){
		
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ContactsEventHandler> getAssociatedType() {
		return TYPE;
	}
}
