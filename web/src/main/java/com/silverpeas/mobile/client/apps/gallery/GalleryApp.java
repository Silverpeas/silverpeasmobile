package com.silverpeas.mobile.client.apps.gallery;

import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.gallery.events.app.internal.AbstractGalleryEvent;
import com.silverpeas.mobile.client.apps.gallery.events.app.internal.GalleryEventHandler;
import com.silverpeas.mobile.client.apps.gallery.events.app.internal.GalleryStopEvent;
import com.silverpeas.mobile.client.apps.gallery.pages.GalleryPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;

public class GalleryApp extends App implements GalleryEventHandler {
	
	public GalleryApp() {
		super();
		EventBus.getInstance().addHandler(AbstractGalleryEvent.TYPE, this);
	}

	@Override
	public void start(Page lauchingPage) {
		setController(new GalleryController());
		setMainPage(new GalleryPage());
		super.start(lauchingPage);
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractGalleryEvent.TYPE, this);
		super.stop();
	}

	@Override
	public void onStop(GalleryStopEvent event) {
		stop();		
	}
}
