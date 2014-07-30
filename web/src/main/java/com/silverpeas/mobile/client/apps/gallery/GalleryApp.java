package com.silverpeas.mobile.client.apps.gallery;

import com.google.gwt.core.client.GWT;
import com.silverpeas.mobile.client.apps.gallery.events.app.AbstractGalleryEvent;
import com.silverpeas.mobile.client.apps.gallery.events.app.GalleryEventHandler;
import com.silverpeas.mobile.client.apps.gallery.pages.MediaNavigationPage;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryMessages;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;

public class GalleryApp extends App implements NavigationEventHandler, GalleryEventHandler {
	
	private GalleryMessages msg;
	private NavigationApp navApp = new NavigationApp();
	
	public GalleryApp() {
		super();
		msg = GWT.create(GalleryMessages.class);
		EventBus.getInstance().addHandler(AbstractGalleryEvent.TYPE, this);
		EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
	}

	@Override
	public void start() {			
		navApp.setTypeApp(Apps.gallery.name());
		navApp.setTitle(msg.title());
		
		navApp.start();
		
		// app main is navigation app main page
		setMainPage(navApp.getMainPage());
		
		super.start();
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractGalleryEvent.TYPE, this);
		EventBus.getInstance().removeHandler(AbstractNavigationEvent.TYPE, this);
		navApp.stop();
		super.stop();		
	}

	@Override
	public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
		MediaNavigationPage page = new MediaNavigationPage();
		page.setPageTitle(msg.title());				
		page.setInstanceId(event.getInstance().getId());
		page.setAlbumId(null);
		page.show();	
		
	}
}
