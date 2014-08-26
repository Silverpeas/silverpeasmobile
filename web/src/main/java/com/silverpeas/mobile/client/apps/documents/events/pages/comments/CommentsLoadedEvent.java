package com.silverpeas.mobile.client.apps.documents.events.pages.comments;

import com.silverpeas.mobile.shared.dto.documents.CommentDTO;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;

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
