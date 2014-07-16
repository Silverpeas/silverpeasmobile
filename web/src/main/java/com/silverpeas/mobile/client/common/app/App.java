package com.silverpeas.mobile.client.common.app;

import java.util.ArrayList;
import java.util.List;

import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.components.base.PageContent;

public abstract class App {

	private PageContent mainPage;
	private PageContent lauchingPage;
	private Controller controller;
	private List<View> pages = new ArrayList<View>();

	public void start(PageContent lauchingPage) {		
		if (SpMobil.currentApp != null) {			
			// stop previous app
			SpMobil.currentApp.stop(); 
		}		
		SpMobil.currentApp = this;
		// display first app page
		this.lauchingPage = lauchingPage;
		lauchingPage.goTo(mainPage); 
	}

	public void stop() {		
		for (View page : pages) {
			if (page != null) page.stop();			
		}
		pages.clear();	
		setMainPage(null);		
		getController().stop();
		setController(null);
	}

	public PageContent getMainPage() {
		return mainPage;
	}	

	protected void setMainPage(PageContent mainPage) {
		this.mainPage = mainPage;
		this.pages.add(mainPage);
	}
	
	protected void addPage(PageContent secondaryPage) {
		this.pages.add(secondaryPage);
	}

	public PageContent getLauchingPage() {
		return lauchingPage;
	}

	public void setLauchingPage(PageContent lauchingPage) {
		this.lauchingPage = lauchingPage;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
}
