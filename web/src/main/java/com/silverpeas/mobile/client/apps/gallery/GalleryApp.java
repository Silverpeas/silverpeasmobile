package com.silverpeas.mobile.client.apps.gallery;

import com.silverpeas.mobile.client.apps.gallery.events.app.AbstractGalleryEvent;
import com.silverpeas.mobile.client.apps.gallery.events.app.GalleryEventHandler;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;

public class GalleryApp extends App implements NavigationEventHandler, GalleryEventHandler {
	
	private NavigationApp navApp = new NavigationApp();
	
	public GalleryApp() {
		super();
		EventBus.getInstance().addHandler(AbstractGalleryEvent.TYPE, this);
		EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
	}

	@Override
	public void start() {
		
		navApp.setTypeApp(Apps.gallery.name());
		navApp.setTitle("Media");
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
		// TODO Auto-generated method stub
		
	}
}
