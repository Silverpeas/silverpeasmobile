package com.silverpeas.mobile.client.apps.documents.events.pages.navigation;

import com.google.gwt.event.shared.EventHandler;

public interface TopicsNavigationPagesEventHandler extends EventHandler {
	void onLoadedTopics(TopicsLoadedEvent event);
	void onTopicSelected(TopicSelectedEvent event);
}