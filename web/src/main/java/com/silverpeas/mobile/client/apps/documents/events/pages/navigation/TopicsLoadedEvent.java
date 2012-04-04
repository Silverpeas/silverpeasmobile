package com.silverpeas.mobile.client.apps.documents.events.pages.navigation;

import java.util.List;

import com.silverpeas.mobile.shared.dto.documents.TopicDTO;

public class TopicsLoadedEvent extends AbstractTopicsNavigationPagesEvent {

	private List<TopicDTO> topics;
	
	public TopicsLoadedEvent(List<TopicDTO> topics) {
		super();
		this.topics = topics;
	}

	@Override
	protected void dispatch(TopicsNavigationPagesEventHandler handler) {
		handler.onLoadedTopics(this);
	}

	public List<TopicDTO> getTopics() {
		return topics;
	}
}
