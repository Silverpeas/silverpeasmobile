package com.silverpeas.mobile.client.apps.comments.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractCommentsPagesEvent extends GwtEvent<CommentsPagesEventHandler> {

	public static Type<CommentsPagesEventHandler> TYPE = new Type<CommentsPagesEventHandler>();
	
	public AbstractCommentsPagesEvent() {
	}
	
	@Override
	public Type<CommentsPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
