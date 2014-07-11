package com.silverpeas.mobile.client.apps.documents;

import com.google.gwt.core.client.GWT;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.components.base.PageContent;

public class DocumentsApp extends App {
	
	private DocumentsMessages msg;
	private NavigationApp navApp = new NavigationApp();
	
	public DocumentsApp() {
		super();
		msg = GWT.create(DocumentsMessages.class);
	}
	
	@Override
	public void start(PageContent lauchingPage) {
		setController(new DocumentsController());	
		
		navApp.setTypeApp(Apps.kmelia.name());
		navApp.setTitle(msg.title());
		navApp.start(lauchingPage);
		
		// app main is navigation app main page
		setMainPage(navApp.getMainPage());
		
		super.start(lauchingPage);
	}

	@Override
	public void stop() {
		navApp.stop();
		super.stop();
	}

}
