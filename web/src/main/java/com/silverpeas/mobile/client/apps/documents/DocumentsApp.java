package com.silverpeas.mobile.client.apps.documents;

import com.google.gwt.core.client.GWT;
import com.silverpeas.mobile.client.apps.documents.pages.GedNavigationPage;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.components.base.PageContent;

public class DocumentsApp extends App implements NavigationEventHandler {
	
	private DocumentsMessages msg;
	private NavigationApp navApp = new NavigationApp();
	
	public DocumentsApp() {
		super();
		msg = GWT.create(DocumentsMessages.class);
		EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
	}
	
	@Override
	public void start() {
		setController(new DocumentsController());	
		
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
		navApp.stop();
		super.stop();
	}
	
	@Override
	public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {		
		GedNavigationPage page = new GedNavigationPage();
		page.setPageTitle(msg.title());				
		page.setInstanceId(event.getInstance().getId());
		page.setTopicId(null);
		addPage(page);
		getMainPage().goTo(page);		
	}

}
