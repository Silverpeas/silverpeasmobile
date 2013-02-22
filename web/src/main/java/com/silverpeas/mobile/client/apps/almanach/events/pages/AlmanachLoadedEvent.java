package com.silverpeas.mobile.client.apps.almanach.events.pages;

import java.util.List;

import com.silverpeas.mobile.shared.dto.EventDetailDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class AlmanachLoadedEvent extends AbstractAlmanachPagesEvent {

	private List<EventDetailDTO> listEventDetailDTO;
	private ApplicationInstanceDTO instance;
	
	public AlmanachLoadedEvent(ApplicationInstanceDTO instance, List<EventDetailDTO> listEventDetailDTO){
		super();
		this.listEventDetailDTO = listEventDetailDTO;
		this.instance = instance;
	}
	
	@Override
	protected void dispatch(AlmanachPagesEventHandler handler) {
		handler.onAlmanachLoaded(this);
	}

	public List<EventDetailDTO> getListEventDetailDTO(){
		return listEventDetailDTO;
	}
	
	public ApplicationInstanceDTO getInstance(){
		return instance;
	}
}
