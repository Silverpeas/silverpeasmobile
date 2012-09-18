package com.silverpeas.mobile.client.apps.contacts.events.controller;


public class ContactsLoadEvent extends AbstractContactsControllerEvent{
	
	private String filter;
	
	public ContactsLoadEvent(String filter){
		super();
		this.filter = filter;
	}

	@Override
	protected void dispatch(ContactsControllerEventHandler handler) {
		handler.loadContacts(this);
	}
	
	public String getFilter(){
		return filter;
	}
}
