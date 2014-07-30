package com.silverpeas.mobile.client.common.app;

import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.components.base.PageContent;

public abstract class App {

	private PageContent mainPage;	
	
	public void start() {
		PageHistory.getInstance().goTo(mainPage);
	}

	public void stop() {			
	}

	public PageContent getMainPage() {
		return mainPage;
	}	

	protected void setMainPage(PageContent mainPage) {		
		this.mainPage = mainPage;	
		this.mainPage.setApp(this);
		
	}
}
