package com.silverpeas.mobile.client.apps.contacts.events.controller;

import com.silverpeas.mobile.shared.dto.contact.ContactFilters;

public class ContactsLoadEvent extends AbstractContactsControllerEvent{
	
	private ContactFilters filter;
	
	public ContactsLoadEvent(ContactFilters filter){
		super();
		this.filter = filter;
	}

	@Override
	protected void dispatch(ContactsControllerEventHandler handler) {
		handler.loadContacts(this);
	}
	
	public ContactFilters getFilter(){
		return filter;
	}
}
