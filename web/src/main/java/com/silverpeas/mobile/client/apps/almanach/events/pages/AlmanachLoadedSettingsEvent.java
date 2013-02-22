package com.silverpeas.mobile.client.apps.almanach.events.pages;

import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class AlmanachLoadedSettingsEvent extends AbstractAlmanachPagesEvent {

	private ApplicationInstanceDTO instance;
	
	public AlmanachLoadedSettingsEvent(ApplicationInstanceDTO instance) {
		super();
		this.instance = instance;
	}

	@Override
	protected void dispatch(AlmanachPagesEventHandler handler) {
		handler.onLoadedSettings(this);
	}

	public ApplicationInstanceDTO getInstance() {
		return instance;
	}

}
