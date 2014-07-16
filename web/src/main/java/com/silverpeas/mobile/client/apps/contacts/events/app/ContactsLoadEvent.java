package com.silverpeas.mobile.client.apps.contacts.events.app;


public class ContactsLoadEvent extends AbstractContactsAppEvent{
	
	private String filter;
	
	public ContactsLoadEvent(String filter){
		super();
		this.filter = filter;
	}

	@Override
	protected void dispatch(ContactsAppEventHandler handler) {
		handler.loadContacts(this);
	}
	
	public String getFilter(){
		return filter;
	}
}
