package com.silverpeas.mobile.client.apps.documents;

import java.util.List;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.documents.events.controller.AbstractDocumentsControllerEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsControllerEventHandler;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadPublicationEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadPublicationsEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadSettingsEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadTopicsEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsSaveSettingsEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.DocumentsLoadedSettingsEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.PublicationsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.TopicsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.persistances.DocumentsSettings;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.documents.TopicDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

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
		//EventBus.getInstance().fireEvent(new NewInstanceLoadedEvent(event.getInstance()));
		Window.alert("show ged explorer!");
	}

	@Override
	public void loadSettings(DocumentsLoadSettingsEvent event) {
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {
			String dataItem = storage.getItem("documentsSettings");			
			if (dataItem != null) {
				DocumentsSettings settings = DocumentsSettings.getInstance(dataItem);				
				
				ApplicationInstanceDTO instance = new ApplicationInstanceDTO();
				instance.setId(settings.getSelectedInstanceId());
				instance.setLabel(settings.getSelectedInstanceLabel());
				instance.setType(Apps.kmelia.name());
				
				TopicDTO topic = new TopicDTO();
				topic.setId(settings.getSelectedTopicId());
				topic.setName(settings.getSelectedTopicLabel());
								
				EventBus.getInstance().fireEvent(new DocumentsLoadedSettingsEvent(instance, topic));
			}
		}
	}
	
	/**
	 * Store settings.
	 */
	@Override
	public void saveSettings(final DocumentsSaveSettingsEvent event) {
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {
			DocumentsSettings settings = new DocumentsSettings(event.getInstance().getId(), event.getInstance().getLabel());						
			if (event.getTopic() != null) {
				settings.setSelectedTopicId(event.getTopic().getId());
				settings.setSelectedTopicLabel(event.getTopic().getName());
			}						
			storage.setItem("documentsSettings", settings.toJson());			
		}
	}

	/**
	 * Get subtopics.
	 */
	@Override
	public void loadTopics(DocumentsLoadTopicsEvent event) {		
		ServicesLocator.serviceDocuments.getTopics(event.getInstanceId(), event.getRootTopicId(), new AsyncCallback<List<TopicDTO>>() {			
			@Override
			public void onSuccess(List<TopicDTO> result) {
				EventBus.getInstance().fireEvent(new TopicsLoadedEvent(result));				
			}			
			@Override
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));				
			}
		});		
	}

	/**
	 * Get publications.
	 */
	@Override
	public void loadPublications(DocumentsLoadPublicationsEvent event) {
		ServicesLocator.serviceDocuments.getPublications(event.getInstanceId(), event.getTopicId(), new AsyncCallback<List<PublicationDTO>>() {			
			@Override
			public void onSuccess(List<PublicationDTO> result) {
				EventBus.getInstance().fireEvent(new PublicationsLoadedEvent(result));				
			}			
			@Override
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));				
			}
		});	
		
	}

	/**
	 * Get publication infos.
	 */
	@Override
	public void loadPublication(DocumentsLoadPublicationEvent event) {
		ServicesLocator.serviceDocuments.getPublication(event.getPubId(), new AsyncCallback<PublicationDTO>() {			
			@Override
			public void onSuccess(PublicationDTO result) {
				EventBus.getInstance().fireEvent(new PublicationLoadedEvent(result));				
			}			
			@Override
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));				
			}
		});			
	}
}
