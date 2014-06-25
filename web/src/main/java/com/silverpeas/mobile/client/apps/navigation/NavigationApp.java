package com.silverpeas.mobile.client.apps.navigation;

import com.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.app.internal.NavigationStopEvent;
import com.silverpeas.mobile.client.apps.navigation.pages.NavigationPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.components.base.PageContent;

public class NavigationApp extends App implements NavigationEventHandler, com.silverpeas.mobile.client.apps.navigation.events.app.internal.NavigationEventHandler {

	private String type, title;
	
	public NavigationApp() {
		super();		
		EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
		EventBus.getInstance().addHandler(com.silverpeas.mobile.client.apps.navigation.events.app.internal.AbstractNavigationEvent.TYPE, this);
	}

	@Override
	public void start(PageContent lauchingPage) {
		setController(new NavigationController(type));
		NavigationPage mainPage = new NavigationPage();
		mainPage.setTitle(title);
		mainPage.setRootSpaceId(null);
		setMainPage(mainPage);		
		super.start(lauchingPage);
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractNavigationEvent.TYPE, this);
		EventBus.getInstance().removeHandler(com.silverpeas.mobile.client.apps.navigation.events.app.internal.AbstractNavigationEvent.TYPE, this);
		super.stop();
	}
	
	public void setTypeApp(String type) {
		this.type = type;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
		stop();
	}

	@Override
	public void onStop(NavigationStopEvent event) {
		stop();		
	}
}
