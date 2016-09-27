package com.silverpeas.mobile.client.apps.comments.events.app;

public class AddCommentEvent extends AbstractCommentsAppEvent {

	private String contentId, instanceId, contentType, message;

	public AddCommentEvent(String contentId, String instanceId, String contentType, String message){
		super();
		this.contentId = contentId;
    this.instanceId = instanceId;
    this.contentType = contentType;
    this.message = message;
	}
	
	@Override
	protected void dispatch(CommentsAppEventHandler handler) {
		handler.addComment(this);
	}
	
	public String getContentId(){
		return contentId;
	}

  public String getContentType() {
    return contentType;
  }

  public String getMessage() {
    return message;
  }

  public String getInstanceId() {
    return instanceId;
  }
}
