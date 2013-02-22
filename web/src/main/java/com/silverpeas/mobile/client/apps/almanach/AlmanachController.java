package com.silverpeas.mobile.client.apps.almanach;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmobile.persistence.client.Collection;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AbstractAlmanachControllerEvent;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachControllerEventHandler;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachLoadEventsEvent;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachLoadSettingsEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedSettingsEvent;
import com.silverpeas.mobile.client.apps.almanach.persistances.AlmanachSettings;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationEventHandler;
import com.silverpeas.mobile.client.common.Database;
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
		Database.open();		
		final Entity<AlmanachSettings> settingsEntity = GWT.create(AlmanachSettings.class);
		final Collection<AlmanachSettings> settings = settingsEntity.all().limit(1);			
		settings.one(new ScalarCallback<AlmanachSettings>() {
			public void onSuccess(final AlmanachSettings settings) {
				ApplicationInstanceDTO instance = new ApplicationInstanceDTO();
				instance.setId(settings.getSelectedInstanceId());
				instance.setLabel(settings.getSelectedInstanceLabel());
				instance.setType(Apps.almanach.name());
												
				EventBus.getInstance().fireEvent(new AlmanachLoadedSettingsEvent(instance));
			}
		});
	}

	public void saveSettings(final ApplicationInstanceDTO instance) {
		Database.open();		
		final Entity<AlmanachSettings> settingsEntity = GWT.create(AlmanachSettings.class);
		final Collection<AlmanachSettings> settings = settingsEntity.all();		
		Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {
			@Override
			public void onSuccess() {
				settings.destroyAll(new com.gwtmobile.persistence.client.Callback() {
					public void onSuccess() {						
						Persistence.flush();
						final Entity<AlmanachSettings> settingsEntity = GWT.create(AlmanachSettings.class);				
						Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {			
							public void onSuccess() {
								final AlmanachSettings settings = settingsEntity.newInstance();
								settings.setSelectedInstanceId(instance.getId());
								settings.setSelectedInstanceLabel(instance.getLabel());								
								Persistence.flush();
							}
						});				
					}
				});				
			}		
		});		
	}

	@Override
	public void loadEvents(AlmanachLoadEventsEvent event) {
		loadAlmanach(event.getInstance());		
	}
}