package com.silverpeas.mobile.client.apps.contacts.events.pages;

import java.util.List;

import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactsByLetterLoadedEvent extends AbstractContactsPagesEvent{

	private List<DetailUserDTO> listDetailUserDTO;
	
	public ContactsByLetterLoadedEvent(List<DetailUserDTO> listUserDetailDTO){
		super();
		this.listDetailUserDTO = listUserDetailDTO;
	}
	
	@Override
	protected void dispatch(ContactsPagesEventHandler handler) {
		handler.onContactsByLetterLoaded(this);
	}
	
	public List<DetailUserDTO> getDetailUserDTO(){
		return listDetailUserDTO;
	}
}
