package com.silverpeas.mobile.client.apps.contacts.events.pages;

import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactDetailLoadedEvent extends AbstractContactsDetailPagesEvent{

	DetailUserDTO detailUserDTO;
	
	public ContactDetailLoadedEvent(DetailUserDTO result){
		super();
		this.detailUserDTO = result;
	}
	
	@Override
	protected void dispatch(ContactDetailPagesEventHandler handler) {
		handler.onContactDetailLoaded(this);
	}
	
	public DetailUserDTO getUserDetailDTO(){
		return detailUserDTO;
	}
}
