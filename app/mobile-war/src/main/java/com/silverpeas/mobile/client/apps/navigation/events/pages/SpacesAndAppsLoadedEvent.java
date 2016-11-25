package com.silverpeas.mobile.client.apps.navigation.events.pages;

import java.util.List;

import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;

public class SpacesAndAppsLoadedEvent extends AbstractNavigationPagesEvent {
	
	private List<SilverpeasObjectDTO> objectsList;
	
	public SpacesAndAppsLoadedEvent(List<SilverpeasObjectDTO> objectsList) {
		super();
		this.objectsList = objectsList;
	}

	@Override
	protected void dispatch(NavigationPagesEventHandler handler) {
		handler.spacesAndAppsLoaded(this);
	}

	public List<SilverpeasObjectDTO> getObjectsList() {
		return objectsList;
	}
}
