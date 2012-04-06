package com.silverpeas.mobile.client.apps.documents.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.AccordionStack;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadPublicationEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.AbstractPublicationPagesEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationNavigationPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;

public class PublicationPage extends Page implements View, PublicationNavigationPagesEventHandler {

	private static PublicationPageUiBinder uiBinder = GWT.create(PublicationPageUiBinder.class);

	private PublicationDTO publication;
	
	@UiField Label title;
	@UiField AccordionStack attachements;
	
	interface PublicationPageUiBinder extends UiBinder<Widget, PublicationPage> {
	}

	public PublicationPage() {
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractPublicationPagesEvent.TYPE, this);
		attachements.expand();
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractPublicationPagesEvent.TYPE, this);	
	}

	@Override
	public void goBack(Object returnValue) {
		stop();
		super.goBack(returnValue);
	}

	public void setPublicationId(String id) {
		// send event to controler for retrieve pub infos
		Notification.activityStart();		
		EventBus.getInstance().fireEvent(new DocumentsLoadPublicationEvent(id));
	}

	@Override
	public void onLoadedPublication(PublicationLoadedEvent event) {
		Notification.activityStop();
		this.publication = event.getPublication();
		display();
	}

	/**
	 * Refesh view informations.
	 */
	private void display() {
		title.setText(publication.getName());
	}
}
