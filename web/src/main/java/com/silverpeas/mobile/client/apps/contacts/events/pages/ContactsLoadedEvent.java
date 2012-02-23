package com.silverpeas.mobile.client.apps.contacts.events.pages;

import java.util.List;

import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactsLoadedEvent extends AbstractContactsPagesEvent{
	
	public ContactsLoadedEvent(List<DetailUserDTO> listDetailUserDTO){
		super();
	}
	
	@Override
	protected void dispatch(ContactsPagesEventHandler handler) {
		handler.onContactsLoaded(this);
	}
}
