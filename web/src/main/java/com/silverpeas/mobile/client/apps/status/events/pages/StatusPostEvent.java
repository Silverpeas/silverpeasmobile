package com.silverpeas.mobile.client.apps.status.events.pages;

public class StatusPostEvent extends AbstractStatusPostPagesEvent{

	private String newStatus;
	
	public StatusPostEvent(String newStatus){
		super();
		this.newStatus = newStatus;
	}
	
	@Override
	protected void dispatch(StatusPostPagesEventHandler handler) {
		handler.onStatusPost(this);
	}
	
	public String getNewStatus(){
		return newStatus;
	}
}
