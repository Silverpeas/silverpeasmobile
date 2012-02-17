package com.silverpeas.mobile.client.apps.contacts.events.app.internal;

public class ContactsStopEvent extends AbstractContactEvent{
	
	public ContactsStopEvent(){
		super();
	}
	
	@Override
	protected void dispatch(ContactsEventHandler handler) {
		handler.onStop(this);
	}
}
