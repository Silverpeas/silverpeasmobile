package com.silverpeas.mobile.client.apps.navigation.events.app;

public class LoadSpacesAndAppsEvent extends AbstractNavigationAppEvent {
	
	private String rootSpaceId;
	
	public LoadSpacesAndAppsEvent(String rootSpaceId) {
		super();
		this.rootSpaceId = rootSpaceId;
	}

	@Override
	protected void dispatch(NavigationAppEventHandler handler) {
		handler.loadSpacesAndApps(this);
	}

	public String getRootSpaceId() {
		return rootSpaceId;
	}
}
