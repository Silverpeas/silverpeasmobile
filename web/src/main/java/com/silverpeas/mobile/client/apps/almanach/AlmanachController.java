package com.silverpeas.mobile.client.apps.almanach;

import java.util.List;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AbstractAlmanachControllerEvent;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachControllerEventHandler;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachLoadEventsEvent;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachLoadSettingsEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedSettingsEvent;
import com.silverpeas.mobile.client.apps.almanach.persistances.AlmanachSettings;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class AlmanachController implements Controller, AlmanachControllerEventHandler, NavigationEventHandler {

	public AlmanachController() {
		super();
		EventBus.getInstance().addHandler(AbstractAlmanachControllerEvent.TYPE,this);
		EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractAlmanachControllerEvent.TYPE, this);
		EventBus.getInstance().removeHandler(AbstractNavigationEvent.TYPE, this);
	}

	public void loadAlmanach(final ApplicationInstanceDTO instance) {
		ServicesLocator.serviceAlmanach.getAlmanach(instance.getId(), new AsyncCallback<List<EventDetailDTO>>() {
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}
			public void onSuccess(List<EventDetailDTO> result) {
				EventBus.getInstance().fireEvent(new AlmanachLoadedEvent(instance, result));
			}
		});
	}

	@Override
	public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
		loadAlmanach(event.getInstance());
		saveSettings(event.getInstance());
	}

	@Override
	public void loadSettings(AlmanachLoadSettingsEvent event) {	
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {
			String dataItem = storage.getItem("almanachSettings");			
			if (dataItem != null) {
				AlmanachSettings settings = AlmanachSettings.getInstance(dataItem);				
				ApplicationInstanceDTO instance = new ApplicationInstanceDTO();
				instance.setId(settings.getSelectedInstanceId());
				instance.setLabel(settings.getSelectedInstanceLabel());
				instance.setType(Apps.almanach.name());												
				EventBus.getInstance().fireEvent(new AlmanachLoadedSettingsEvent(instance));
			}
		}
	}

	public void saveSettings(final ApplicationInstanceDTO instance) {
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {
			AlmanachSettings settings = new AlmanachSettings(instance.getId(), instance.getLabel());			
			storage.setItem("almanachSettings", settings.toJson());			
		}
	}

	@Override
	public void loadEvents(AlmanachLoadEventsEvent event) {
		loadAlmanach(event.getInstance());		
	}
}