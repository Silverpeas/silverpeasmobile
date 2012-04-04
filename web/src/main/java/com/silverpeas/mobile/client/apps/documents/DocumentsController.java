package com.silverpeas.mobile.client.apps.documents;

import com.google.gwt.core.client.GWT;
import com.gwtmobile.persistence.client.Collection;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.silverpeas.mobile.client.apps.documents.events.controller.AbstractDocumentsControllerEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsControllerEventHandler;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadSettingsEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.DocumentsLoadedSettingsEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.NewInstanceLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.persistances.DocumentsSettings;
import com.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationEventHandler;
import com.silverpeas.mobile.client.common.Database;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.Controller;

public class DocumentsController implements Controller, DocumentsControllerEventHandler, NavigationEventHandler{

	public DocumentsController() {
		super();
		EventBus.getInstance().addHandler(AbstractDocumentsControllerEvent.TYPE, this);
		EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
	}
	
	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractDocumentsControllerEvent.TYPE, this);
		EventBus.getInstance().removeHandler(AbstractNavigationEvent.TYPE, this);		
	}

	@Override
	public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
		EventBus.getInstance().fireEvent(new NewInstanceLoadedEvent(event.getInstance()));		
	}

	@Override
	public void loadSettings(DocumentsLoadSettingsEvent event) {
		Database.open();		
		final Entity<DocumentsSettings> settingsEntity = GWT.create(DocumentsSettings.class);
		final Collection<DocumentsSettings> settings = settingsEntity.all().limit(1);			
		settings.one(new ScalarCallback<DocumentsSettings>() {
			public void onSuccess(final DocumentsSettings settings) {				
				EventBus.getInstance().fireEvent(new DocumentsLoadedSettingsEvent(settings));
			}
		});		
	}

}
