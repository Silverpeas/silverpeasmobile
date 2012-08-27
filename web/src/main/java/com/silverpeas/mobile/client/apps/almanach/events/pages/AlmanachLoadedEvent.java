<<<<<<< HEAD
package com.silverpeas.mobile.client.apps.almanach.events.pages;

import java.util.Collection;

import com.silverpeas.mobile.shared.dto.EventDetailDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class AlmanachLoadedEvent extends AbstractAlmanachPagesEvent {

	Collection<EventDetailDTO> listEventDetailDTO;
	ApplicationInstanceDTO instance;
	
	public AlmanachLoadedEvent(ApplicationInstanceDTO instance, Collection<EventDetailDTO> listEventDetailDTO){
		super();
		this.listEventDetailDTO = listEventDetailDTO;
		this.instance = instance;
	}
	
	@Override
	protected void dispatch(AlmanachPagesEventHandler handler) {
		handler.onAlmanachLoaded(this);
	}

	public Collection<EventDetailDTO> getListEventDetailDTO(){
		return listEventDetailDTO;
	}
	
	public ApplicationInstanceDTO getInstance(){
		return instance;
	}
}
=======
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
>>>>>>> 09b477a4f719202ea331d7ac57a295f981f9520f
