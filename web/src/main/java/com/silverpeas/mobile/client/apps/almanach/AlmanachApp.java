package com.silverpeas.mobile.client.apps.almanach;

import com.silverpeas.mobile.client.apps.almanach.events.app.internal.AbstractAlmanachEvent;
import com.silverpeas.mobile.client.apps.almanach.events.app.internal.AlmanachEventHandler;
import com.silverpeas.mobile.client.apps.almanach.events.app.internal.AlmanachStopEvent;
import com.silverpeas.mobile.client.apps.almanach.pages.AlmanachPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;

public class AlmanachApp extends App implements AlmanachEventHandler{
	public AlmanachApp(){
		super();
		EventBus.getInstance().addHandler(AbstractAlmanachEvent.TYPE, this);
	}
	
	public void start(){
		setController(new AlmanachController());
		setMainPage(new AlmanachPage());
		super.start();
	}
	
	@Override
	public void stop(){
		EventBus.getInstance().removeHandler(AbstractAlmanachEvent.TYPE, this);
		super.stop();
	}
	
	@Override
	public void onStop(AlmanachStopEvent event) {
		stop();
	}
}
