package com.silverpeas.mobile.client.common.app;

import com.silverpeas.mobile.client.components.base.PageContent;

public abstract class App {

	private PageContent mainPage;
	private PageContent lauchingPage;
	private Controller controller;

	public void start(PageContent lauchingPage) {		
		this.lauchingPage = lauchingPage;
		lauchingPage.goTo(mainPage);
	}

	public void stop() {
		setMainPage(null);		
		getController().stop();
		setController(null);
	}

	protected PageContent getMainPage() {
		return mainPage;
	}	

	protected void setMainPage(PageContent mainPage) {
		this.mainPage = mainPage;
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
