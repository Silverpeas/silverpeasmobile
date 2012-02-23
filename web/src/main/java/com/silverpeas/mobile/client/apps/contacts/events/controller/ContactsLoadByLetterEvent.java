package com.silverpeas.mobile.client.apps.contacts.events.controller;

public class ContactsLoadByLetterEvent extends AbstractContactsControllerEvent{

	private String filtre;
	
	public ContactsLoadByLetterEvent(String filtre){
		super();
		this.filtre = filtre;
	}
	
	@Override
	protected void dispatch(ContactsControllerEventHandler handler) {
		handler.loadContactsByLetter(this);
	}
	
	public String getFiltre(){
		return filtre;
	}
}
