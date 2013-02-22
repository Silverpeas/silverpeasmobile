package com.silverpeas.mobile.client.apps.almanach.events.controller;

import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;


public class AlmanachLoadEventsEvent extends AbstractAlmanachControllerEvent {
	
	private ApplicationInstanceDTO instance;
	
	public AlmanachLoadEventsEvent(ApplicationInstanceDTO instance) {
		super();
		this.instance = instance;
	}

	@Override
	protected void dispatch(AlmanachControllerEventHandler handler) {
		handler.loadEvents(this);
	}
	
	public ApplicationInstanceDTO getInstance() {
		return instance;
	}
}
