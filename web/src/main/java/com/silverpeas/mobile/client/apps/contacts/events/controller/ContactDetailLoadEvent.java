package com.silverpeas.mobile.client.apps.contacts.events.controller;

public class ContactDetailLoadEvent extends AbstractContactsControllerEvent{

	private String id;
	
	public ContactDetailLoadEvent(String id){
		super();
		this.id = id;
	}

	@Override
	protected void dispatch(ContactsControllerEventHandler handler) {
		handler.loadContactDetail(this);
	}
	
	public String getId(){
		return id;
	}
}
