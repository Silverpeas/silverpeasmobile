package com.silverpeas.mobile.client.apps.contacts.events.app;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractContactsAppEvent extends GwtEvent<ContactsAppEventHandler>{

	public static Type<ContactsAppEventHandler> TYPE = new Type<ContactsAppEventHandler>();
	
	public AbstractContactsAppEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ContactsAppEventHandler> getAssociatedType() {
		return TYPE;
	}
}
