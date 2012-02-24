package com.silverpeas.mobile.client.apps.status.events.pages;

public class StatusPostedEvent extends AbstractStatusPostPagesEvent{

	private String newStatus;
	
	public StatusPostedEvent(String newStatus){
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
