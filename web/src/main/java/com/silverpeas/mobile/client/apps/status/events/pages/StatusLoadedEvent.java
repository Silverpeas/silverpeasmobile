package com.silverpeas.mobile.client.apps.status.events.pages;

import java.util.List;

import com.silverpeas.mobile.shared.dto.StatusDTO;

public class StatusLoadedEvent extends AbstractStatusPagesEvent{

	private List<StatusDTO> listStatus;
	
	public StatusLoadedEvent(List<StatusDTO> listStatus){
		super();
		this.listStatus = listStatus;
	}
	
	@Override
	protected void dispatch(StatusPagesEventHandler handler) {
		handler.onStatusLoaded(this);
	}
	
	public List<StatusDTO> getListStatus(){
		return listStatus;
	}
}
