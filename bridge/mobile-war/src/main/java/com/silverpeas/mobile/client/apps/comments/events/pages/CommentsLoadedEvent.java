package com.silverpeas.mobile.client.apps.comments.events.pages;

import com.silverpeas.mobile.shared.dto.comments.CommentDTO;

import java.util.List;

public class CommentsLoadedEvent extends AbstractCommentsPagesEvent {

	private List<CommentDTO> comments;
	
	public CommentsLoadedEvent(List<CommentDTO> comments) {
		super();
		this.comments = comments;
	}

	@Override
	protected void dispatch(CommentsPagesEventHandler handler) {
		handler.onLoadedComments(this);
	}

	public List<CommentDTO> getComments() {
		return comments;
	}	
}
