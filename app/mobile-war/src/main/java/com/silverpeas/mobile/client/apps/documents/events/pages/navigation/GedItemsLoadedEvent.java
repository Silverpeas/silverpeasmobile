package com.silverpeas.mobile.client.apps.documents.events.pages.navigation;

import java.util.List;

import com.silverpeas.mobile.shared.dto.BaseDTO;

public class GedItemsLoadedEvent extends AbstractGedNavigationPagesEvent {

	private List<BaseDTO> topicsAndPublications;
	
	public GedItemsLoadedEvent(List<BaseDTO> topicsAndPublications) {
		super();
		this.topicsAndPublications = topicsAndPublications;
	}

	@Override
	protected void dispatch(GedNavigationPagesEventHandler handler) {
		handler.onLoadedTopics(this);
	}

	public List<BaseDTO> getTopicsAndPublications() {
		return topicsAndPublications;
	}
}
