package com.silverpeas.mobile.client.apps.status;

import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.status.events.app.internal.AbstractStatusEvent;
import com.silverpeas.mobile.client.apps.status.events.app.internal.StatusEventHandler;
import com.silverpeas.mobile.client.apps.status.events.app.internal.StatusStopEvent;
import com.silverpeas.mobile.client.apps.status.pages.StatusPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;

public class StatusApp extends App implements StatusEventHandler{
	
	public StatusApp(){
		super();
		EventBus.getInstance().addHandler(AbstractStatusEvent.TYPE, this);
	}
	
	public void start(Page lauchingPage){
		setController(new StatusController());
		setMainPage(new StatusPage());
		super.start(lauchingPage);
	}
	
	@Override
	public void stop(){
		EventBus.getInstance().removeHandler(AbstractStatusEvent.TYPE, this);
		super.stop();
	}

	@Override
	public void onStop(StatusStopEvent event) {
		stop();
	}
}
