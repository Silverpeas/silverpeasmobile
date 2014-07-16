package com.silverpeas.mobile.client.apps.navigation;

import com.silverpeas.mobile.client.apps.navigation.pages.NavigationPage;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.components.base.PageContent;

public class NavigationApp extends App {

	private String type, title;
	
	public NavigationApp() {
		super();		
	}

	@Override
	public void start(PageContent lauchingPage) {
		setController(new NavigationController(type));
		NavigationPage mainPage = new NavigationPage();
		mainPage.setPageTitle(title);
		mainPage.setRootSpaceId(null);
		setMainPage(mainPage);		
		// no "super.start(lauchingPage);" this app is used in another app 
	}
	
	public void setTypeApp(String type) {
		this.type = type;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
