package com.silverpeas.mobile.client.apps.almanach.events.controller;

public class AlmanachLoadEvent extends AbstractAlmanachControllerEvent{
	
	private int month;

	public AlmanachLoadEvent(int month){
		super();
		this.month = month;
	}

	@Override
	protected void dispatch(AlmanachControllerEventHandler handler) {
		handler.loadAlmanach(this);
	}
	
	public void setMonth(int month){
		this.month = month;
	}
	
	public int getMonth(){
		return month;
	}
}
