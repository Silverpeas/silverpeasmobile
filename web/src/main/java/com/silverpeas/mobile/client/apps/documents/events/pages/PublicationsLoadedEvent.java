package com.silverpeas.mobile.client.apps.documents.events.pages;

import java.util.List;

import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;

public class PublicationsLoadedEvent extends AbstractDocumentsPagesEvent {

	private List<PublicationDTO> publications;
	
	public PublicationsLoadedEvent(List<PublicationDTO> publications) {
		super();
		this.publications = publications;
	}

	@Override
	protected void dispatch(DocumentsPagesEventHandler handler) {
		handler.onLoadedPublications(this);
	}

	public List<PublicationDTO> getPublications() {
		return publications;
	}
}
