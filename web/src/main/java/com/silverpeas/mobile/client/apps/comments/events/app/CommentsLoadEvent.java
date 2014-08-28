package com.silverpeas.mobile.client.apps.comments.events.app;

public class CommentsLoadEvent extends AbstractCommentsAppEvent {

	private String contentId;
	
	public CommentsLoadEvent(String contentId){
		super();
		this.contentId = contentId;
	}
	
	@Override
	protected void dispatch(CommentsAppEventHandler handler) {
		handler.loadComments(this);
	}
	
	public String getContentId(){
		return contentId;
	}
}
