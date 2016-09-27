package com.silverpeas.mobile.client.apps.navigation.events.app.external;

import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class NavigationAppInstanceChangedEvent extends AbstractNavigationEvent {

	private ApplicationInstanceDTO instance;

	public NavigationAppInstanceChangedEvent(ApplicationInstanceDTO instance) {
		super();
		this.instance = instance;
	}

	@Override
	protected void dispatch(NavigationEventHandler handler) {
		handler.appInstanceChanged(this);
	}

	public ApplicationInstanceDTO getInstance() {
		return instance;
	}

}