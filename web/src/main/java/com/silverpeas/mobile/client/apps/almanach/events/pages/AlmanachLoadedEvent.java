package com.silverpeas.mobile.client.apps.almanach.events.pages;

import java.util.Collection;

import com.silverpeas.mobile.shared.dto.EventDetailDTO;

public class AlmanachLoadedEvent extends AbstractAlmanachPagesEvent{
	
	private Collection<EventDetailDTO> listEventDetailDTO;

	public AlmanachLoadedEvent(Collection<EventDetailDTO> listEventDetailDTO){
		super();
		this.listEventDetailDTO = listEventDetailDTO;
	}
	
	@Override
	protected void dispatch(AlmanachPagesEventHandler handler) {
		handler.onAlmanachLoaded(this);
	}
	
	public Collection<EventDetailDTO> getListEventDetailDTO(){
		return listEventDetailDTO;
	}
}
