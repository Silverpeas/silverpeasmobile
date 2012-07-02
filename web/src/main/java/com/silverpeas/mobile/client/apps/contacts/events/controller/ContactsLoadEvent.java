package com.silverpeas.mobile.client.apps.contacts.events.controller;

public class ContactsLoadEvent extends AbstractContactsControllerEvent{
	
	public ContactsLoadEvent(){
		super();
	}

	@Override
	protected void dispatch(ContactsControllerEventHandler handler) {
		handler.loadContacts(this);
	}
}
