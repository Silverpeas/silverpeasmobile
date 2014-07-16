package com.silverpeas.mobile.client.common.app;

import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.components.base.PageContent;

public abstract class App {

	private PageContent mainPage;	
	
	public void start() {		
		if (SpMobil.currentApp != null) {			
			// stop previous app
			SpMobil.currentApp.stop(); 
		}		
		SpMobil.currentApp = this;
		// display first app page
		PageHistory.getInstance().goTo(mainPage);
	}

	public void stop() {		
		setMainPage(null);	
	}

	public PageContent getMainPage() {
		return mainPage;
	}	

	protected void setMainPage(PageContent mainPage) {
		this.mainPage = mainPage;		
	}
}
