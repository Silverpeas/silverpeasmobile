package com.silverpeas.mobile.client.apps.comments.events.pages;

import com.silverpeas.mobile.shared.dto.comments.CommentDTO;

public class CommentAddedEvent extends AbstractCommentsPagesEvent {

	private CommentDTO comment;

	public CommentAddedEvent(CommentDTO comment) {
		super();
		this.comment = comment;
	}

	@Override
	protected void dispatch(CommentsPagesEventHandler handler) {
		handler.onAddedComment(this);
	}

	public CommentDTO getComment() {
		return comment;
	}	
}
