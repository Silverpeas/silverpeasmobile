package com.silverpeas.mobile.client.apps.dashboard;

import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.dashboard.events.app.internal.AbstractDashboardEvent;
import com.silverpeas.mobile.client.apps.dashboard.events.app.internal.DashboardEventHandler;
import com.silverpeas.mobile.client.apps.dashboard.events.app.internal.DashboardStopEvent;
import com.silverpeas.mobile.client.apps.dashboard.pages.DashboardPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;

public class DashboardApp extends App implements DashboardEventHandler{

	public DashboardApp(){
		super();
		EventBus.getInstance().addHandler(AbstractDashboardEvent.TYPE, this);
	}
	
	public void Start(Page lauchingPage){
		setController(new DashboardController());
		setMainPage(new DashboardPage());
		super.start(lauchingPage);
	}
	
	@Override
	public void stop(){
		EventBus.getInstance().removeHandler(AbstractDashboardEvent.TYPE, this);
		super.stop();
	}
	
	@Override
	public void onStop(DashboardStopEvent event) {
		stop();
	}
}
