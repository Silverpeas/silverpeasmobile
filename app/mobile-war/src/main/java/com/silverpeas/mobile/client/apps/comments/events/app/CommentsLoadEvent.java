package com.silverpeas.mobile.client.apps.comments.events.app;

public class CommentsLoadEvent extends AbstractCommentsAppEvent {

	private String contentId, contentType;
	
	public CommentsLoadEvent(String contentId, String contentType){
		super();
		this.contentId = contentId;
    this.contentType = contentType;
	}
	
	@Override
	protected void dispatch(CommentsAppEventHandler handler) {
		handler.loadComments(this);
	}
	
	public String getContentId(){
		return contentId;
	}

  public String getContentType() {
    return contentType;
  }
}
