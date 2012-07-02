package com.silverpeas.mobile.client.apps.status.events.pages;

import com.silverpeas.mobile.shared.dto.StatusDTO;

public class StatusPostedEvent extends AbstractStatusPagesEvent {

	private StatusDTO newStatus;
	
	public StatusPostedEvent(StatusDTO newStatus){
		super();
		this.newStatus = newStatus;
	}
	
	@Override
	protected void dispatch(StatusPagesEventHandler handler) {
		handler.onStatusPost(this);
	}
	
	public StatusDTO getNewStatus(){
		return newStatus;
	}
}
