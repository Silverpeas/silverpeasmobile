package com.silverpeas.mobile.client.apps.documents.events.pages.comments;

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
