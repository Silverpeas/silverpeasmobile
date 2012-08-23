package com.silverpeas.mobile.client.apps.almanach.events.app.internal;

public class AlmanachStopEvent extends AbstractAlmanachEvent{

	public AlmanachStopEvent(){
		super();
	}
	
	@Override
	protected void dispatch(AlmanachEventHandler handler) {
		handler.onStop(this);
	}
}
