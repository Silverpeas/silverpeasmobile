package com.silverpeas.mobile.client.apps.contacts.events.pages;

public class ContactDetailLoadedEvent extends AbstractContactsDetailPagesEvent{

	String id;
	
	public ContactDetailLoadedEvent(String id){
		super();
		this.id = id;
	}
	
	@Override
	protected void dispatch(ContactDetailPagesEventHandler handler) {
		handler.onContactDetailLoaded(this);
	}
	
	public String getId(){
		return id;
	}
}
