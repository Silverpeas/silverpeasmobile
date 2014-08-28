package com.silverpeas.mobile.client.apps.comments.events.app;

import com.google.gwt.event.shared.EventHandler;

public interface CommentsAppEventHandler extends EventHandler{
	void loadComments(CommentsLoadEvent event);
}
