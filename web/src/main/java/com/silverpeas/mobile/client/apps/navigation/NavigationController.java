package com.silverpeas.mobile.client.apps.navigation;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.navigation.events.controller.AbstractNavigationControllerEvent;
import com.silverpeas.mobile.client.apps.navigation.events.controller.LoadSpacesAndAppsEvent;
import com.silverpeas.mobile.client.apps.navigation.events.controller.NavigationControllerEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.pages.SpacesAndAppsLoadedEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;

public class NavigationController implements Controller, NavigationControllerEventHandler {

	private String type;
	
	public NavigationController(String type) {
		super();
		EventBus.getInstance().addHandler(AbstractNavigationControllerEvent.TYPE, this);
		this.type = type;
	}

	@Override
	public void loadSpacesAndApps(LoadSpacesAndAppsEvent event) {		
		ServicesLocator.serviceNavigation.getSpacesAndApps(event.getRootSpaceId(), type, new AsyncCallback<List<SilverpeasObjectDTO>>() {
			
			@Override
			public void onSuccess(List<SilverpeasObjectDTO> result) {				
				EventBus.getInstance().fireEvent(new SpacesAndAppsLoadedEvent(result));				
			}
			
			@Override
			public void onFailure(Throwable caught) {				
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));				
			}
		});		
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractNavigationControllerEvent.TYPE, this);	
	}
}
