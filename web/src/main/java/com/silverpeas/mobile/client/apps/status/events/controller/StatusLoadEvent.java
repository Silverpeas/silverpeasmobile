package com.silverpeas.mobile.client.apps.status.events.controller;

public class StatusLoadEvent extends AbstractStatusControllerEvent{
	
	private int currentPage;
	
	public StatusLoadEvent(int currentPage){
		super();
		this.currentPage = currentPage;
	}

	@Override
	protected void dispatch(StatusControllerEventHandler handler) {
		handler.loadStatus(this);
	}
	
	public int getCurrentPage(){
		return currentPage;
	}
}
