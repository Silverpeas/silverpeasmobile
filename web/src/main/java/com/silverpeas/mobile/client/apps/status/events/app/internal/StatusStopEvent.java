package com.silverpeas.mobile.client.apps.status.events.app.internal;

public class StatusStopEvent extends AbstractStatusEvent{

	public StatusStopEvent(){
		super();
	}

	@Override
	protected void dispatch(StatusEventHandler handler) {
		handler.onStop(this);
	}
}
