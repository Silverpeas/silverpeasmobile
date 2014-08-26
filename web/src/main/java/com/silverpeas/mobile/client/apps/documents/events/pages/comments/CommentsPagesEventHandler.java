package com.silverpeas.mobile.client.apps.documents.events.pages.comments;

import com.google.gwt.event.shared.EventHandler;

public interface CommentsPagesEventHandler extends EventHandler {
	void onLoadedComments(CommentsLoadedEvent event);
}