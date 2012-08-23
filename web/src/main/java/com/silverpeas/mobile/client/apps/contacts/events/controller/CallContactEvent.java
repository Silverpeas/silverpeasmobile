package com.silverpeas.mobile.client.apps.contacts.events.controller;

public class CallContactEvent extends AbstractContactsControllerEvent{

	public CallContactEvent(){
		super();
	}
	
	@Override
	protected void dispatch(ContactsControllerEventHandler handler) {
		handler.callContact(this);
	}
}
