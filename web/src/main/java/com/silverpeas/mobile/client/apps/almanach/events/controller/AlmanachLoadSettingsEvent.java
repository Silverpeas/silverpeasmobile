package com.silverpeas.mobile.client.apps.almanach.events.controller;

public class AlmanachLoadSettingsEvent extends AbstractAlmanachControllerEvent {
	
	public AlmanachLoadSettingsEvent() {
		super();
	}

	@Override
	protected void dispatch(AlmanachControllerEventHandler handler) {
		handler.loadSettings(this);
	}
}
