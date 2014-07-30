package com.silverpeas.mobile.client.apps.documents;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.documents.events.app.AbstractDocumentsAppEvent;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsAppEventHandler;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadGedItemsEvent;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadPublicationEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.pages.GedNavigationPage;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;

public class DocumentsApp extends App implements NavigationEventHandler, DocumentsAppEventHandler {
	
	private DocumentsMessages msg;
	private NavigationApp navApp = new NavigationApp();
	
	public DocumentsApp() {
		super();
		msg = GWT.create(DocumentsMessages.class);
		EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
		EventBus.getInstance().addHandler(AbstractDocumentsAppEvent.TYPE, this);
	}
	
	@Override
	public void start() {
		navApp.setTypeApp(Apps.kmelia.name());
		navApp.setTitle(msg.title());
		navApp.start();
		
		// app main is navigation app main page
		setMainPage(navApp.getMainPage());
		
		super.start();
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractNavigationEvent.TYPE, this);
		EventBus.getInstance().removeHandler(AbstractDocumentsAppEvent.TYPE, this);
		navApp.stop();
		super.stop();
	}
	
	@Override
	public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
		GedNavigationPage page = new GedNavigationPage();
		page.setPageTitle(msg.title());				
		page.setInstanceId(event.getInstance().getId());
		page.setTopicId(null);
		page.show();		
	}
	
	/**
	 * Get subtopics.
	 */
	@Override
	public void loadTopics(DocumentsLoadGedItemsEvent event) {		
		ServicesLocator.serviceDocuments.getTopicsAndPublications(event.getInstanceId(), event.getRootTopicId(), new AsyncCallback<List<Object>>() {			
			@Override
			public void onSuccess(List<Object> result) {
				EventBus.getInstance().fireEvent(new GedItemsLoadedEvent(result));				
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
