package com.silverpeas.mobile.client.apps.almanach.events.pages;

import java.util.List;

import com.silverpeas.mobile.shared.dto.EventDetailDTO;

public class LoadEventDetailDTOEvent extends AbstractAlmanachPagesEvent{
	
	private List<EventDetailDTO> listEventDetailDTO;
	
	public LoadEventDetailDTOEvent(List<EventDetailDTO> listEventDetailDTO){
		super();
		this.listEventDetailDTO = listEventDetailDTO;
	}

	@Override
	protected void dispatch(AlmanachPagesEventHandler handler) {
		handler.onLoadEventDetailDTOLoaded(this);
	}

	public List<EventDetailDTO> getListEventDetailDTO(){
		return listEventDetailDTO;
	}
}
