package com.silverpeas.mobile.client.apps.contacts.events.controller;

import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class AddContactEvent extends AbstractContactsControllerEvent{
	
	private DetailUserDTO detailUserDTO;
	
	public AddContactEvent(DetailUserDTO detailUserDTO){
		super();
		this.detailUserDTO = detailUserDTO;
	}
	
	protected void dispatch(ContactsControllerEventHandler handler) {
		handler.addContact(this);
	}
	
	public DetailUserDTO getUserDetailDTO(){
		return detailUserDTO;
	}
}
