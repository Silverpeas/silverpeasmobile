package com.silverpeas.mobile.client.apps.comments.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface CommentsPagesEventHandler extends EventHandler {
	void onLoadedComments(CommentsLoadedEvent event);
}