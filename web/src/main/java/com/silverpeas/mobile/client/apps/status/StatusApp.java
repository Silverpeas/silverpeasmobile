package com.silverpeas.mobile.client.apps.status;

import com.silverpeas.mobile.client.apps.status.pages.StatusPage;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.components.base.PageContent;

public class StatusApp extends App {
	
	public StatusApp(){
		super();
	}
	
	public void start(PageContent lauchingPage){
		setController(new StatusController());
		setMainPage(new StatusPage());
		super.start(lauchingPage);
	}
}
