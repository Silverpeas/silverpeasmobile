package com.silverpeas.mobile.client.common.app;

import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.Transition;

public abstract class App {
	
	private Page mainPage;
	private Page lauchingPage;
	private Controller controller;
	protected Transition transition = Transition.SLIDEDOWN;

	public void start(Page lauchingPage) {		
		this.lauchingPage = lauchingPage;
		lauchingPage.goTo(mainPage, transition);
	}
	
	public void stop() {
		setMainPage(null);		
		getController().stop();
		setController(null);
	}
	
	protected Page getMainPage() {
		return mainPage;
	}	
	
	protected void setMainPage(Page mainPage) {
		this.mainPage = mainPage;
	}

	public Page getLauchingPage() {
		return lauchingPage;
	}

	public void setLauchingPage(Page lauchingPage) {
		this.lauchingPage = lauchingPage;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
}
