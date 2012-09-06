package com.silverpeas.mobile.client.apps.contacts.events.controller;

public class ContactsLoadEvent extends AbstractContactsControllerEvent{
	
	private String checkBox;
	
	public ContactsLoadEvent(String checkBox){
		super();
		this.checkBox = checkBox;
	}

	@Override
	protected void dispatch(ContactsControllerEventHandler handler) {
		handler.loadContacts(this);
	}
	
	public String getCheckBox(){
		return checkBox;
	}
}
