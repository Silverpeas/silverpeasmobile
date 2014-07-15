package com.silverpeas.mobile.client.apps.documents.events.pages.navigation;

import java.util.List;

public class GedItemsLoadedEvent extends AbstractGedNavigationPagesEvent {

	private List<Object> topicsAndPublications;
	
	public GedItemsLoadedEvent(List<Object> topicsAndPublications) {
		super();
		this.topicsAndPublications = topicsAndPublications;
	}

	@Override
	protected void dispatch(GedNavigationPagesEventHandler handler) {
		handler.onLoadedTopics(this);
	}

	public List<Object> getTopicsAndPublications() {
		return topicsAndPublications;
	}
}
