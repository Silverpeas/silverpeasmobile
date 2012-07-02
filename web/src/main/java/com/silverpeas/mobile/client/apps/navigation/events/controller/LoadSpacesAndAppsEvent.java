package com.silverpeas.mobile.client.apps.navigation.events.controller;

public class LoadSpacesAndAppsEvent extends AbstractNavigationControllerEvent {
	
	private String rootSpaceId;
	
	public LoadSpacesAndAppsEvent(String rootSpaceId) {
		super();
		this.rootSpaceId = rootSpaceId;
	}

	@Override
	protected void dispatch(NavigationControllerEventHandler handler) {
		handler.loadSpacesAndApps(this);
	}

	public String getRootSpaceId() {
		return rootSpaceId;
	}
}
