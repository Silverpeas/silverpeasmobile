package com.silverpeas.mobile.client.apps.contacts.events.controller;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractContactsControllerEvent extends GwtEvent<ContactsControllerEventHandler>{

	public static Type<ContactsControllerEventHandler> TYPE = new Type<ContactsControllerEventHandler>();
	
	public AbstractContactsControllerEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ContactsControllerEventHandler> getAssociatedType() {
		return TYPE;
	}
}
