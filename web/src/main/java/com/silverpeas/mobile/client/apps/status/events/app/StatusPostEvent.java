package com.silverpeas.mobile.client.apps.status.events.app;

public class StatusPostEvent extends AbstractStatusAppEvent{

	private String postStatus;
	
	public StatusPostEvent(String postStatus){
		super();
		this.postStatus = postStatus;
	}
	
	@Override
	protected void dispatch(StatusAppEventHandler handler) {
		handler.postStatus(this);
	}
	
	public String getPostStatus(){
		return postStatus;
	}
}
