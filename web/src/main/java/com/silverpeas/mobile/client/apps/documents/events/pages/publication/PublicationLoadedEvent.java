package com.silverpeas.mobile.client.apps.documents.events.pages.publication;

import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;

public class PublicationLoadedEvent extends AbstractPublicationPagesEvent {

	private PublicationDTO publication;
	
	public PublicationLoadedEvent(PublicationDTO publication) {
		super();
		this.publication = publication;
	}

	@Override
	protected void dispatch(PublicationNavigationPagesEventHandler handler) {
		handler.onLoadedPublication(this);
	}

	public PublicationDTO getPublication() {
		return publication;
	}	
}
