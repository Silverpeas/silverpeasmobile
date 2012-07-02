package com.silverpeas.mobile.client.apps.contacts.events.pages;

import java.util.List;

import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactsLoadedEvent extends AbstractContactsPagesEvent{
	
	List<DetailUserDTO> listDetailUserDTO;
	
	public ContactsLoadedEvent(List<DetailUserDTO> listDetailUserDTO){
		super();
		this.listDetailUserDTO = listDetailUserDTO;
	}
	
	@Override
	protected void dispatch(ContactsPagesEventHandler handler) {
		handler.onContactsLoaded(this);
	}
	
	public List<DetailUserDTO> getListUserDetailDTO(){
		return listDetailUserDTO;
	}
}
