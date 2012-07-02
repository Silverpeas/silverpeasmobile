package com.silverpeas.mobile.client.apps.status.events.controller;

public class StatusPostEvent extends AbstractStatusControllerEvent{

	private String postStatus;
	
	public StatusPostEvent(String postStatus){
		super();
		this.postStatus = postStatus;
	}
	
	@Override
	protected void dispatch(StatusControllerEventHandler handler) {
		handler.postStatus(this);
	}
	
	public String getPostStatus(){
		return postStatus;
	}
}
