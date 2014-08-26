package com.silverpeas.mobile.client.apps.documents.events.app;

import com.google.gwt.event.shared.EventHandler;

public interface DocumentsAppEventHandler extends EventHandler {	
	void loadTopics(DocumentsLoadGedItemsEvent event);
	void loadPublication(DocumentsLoadPublicationEvent event);
  void loadComments(DocumentsLoadCommentsEvent event);
}