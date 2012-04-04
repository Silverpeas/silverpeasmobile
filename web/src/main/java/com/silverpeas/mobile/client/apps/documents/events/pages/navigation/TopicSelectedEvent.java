package com.silverpeas.mobile.client.apps.documents.events.pages.navigation;

import com.silverpeas.mobile.shared.dto.documents.TopicDTO;

public class TopicSelectedEvent extends AbstractTopicsNavigationPagesEvent {

	private TopicDTO topic;
	
	public TopicSelectedEvent(TopicDTO topic) {
		super();
		this.topic = topic;
	}

	@Override
	protected void dispatch(TopicsNavigationPagesEventHandler handler) {
		handler.onTopicSelected(this);
	}

	public TopicDTO getTopic() {
		return topic;
	}
}
