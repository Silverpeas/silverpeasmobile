package com.silverpeas.mobile.client.apps.comments.events.app;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractCommentsAppEvent extends GwtEvent<CommentsAppEventHandler>{

	public static Type<CommentsAppEventHandler> TYPE = new Type<CommentsAppEventHandler>();
	
	public AbstractCommentsAppEvent(){
	}
	
	@Override
	public Type<CommentsAppEventHandler> getAssociatedType() {
		return TYPE;
	}
}
