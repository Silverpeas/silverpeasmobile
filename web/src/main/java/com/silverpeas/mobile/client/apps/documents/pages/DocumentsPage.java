package com.silverpeas.mobile.client.apps.documents.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.documents.events.app.internal.DocumentsStopEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.AbstractDocumentsPagesEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.DocumentsLoadedSettingsEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.DocumentsPagesEventHandler;
import com.silverpeas.mobile.client.apps.documents.events.pages.NewInstanceLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

/**
 * Documents mobile application.
 * @author svuillet
 */
public class DocumentsPage extends Page implements View, DocumentsPagesEventHandler {

	private static DocumentsPageUiBinder uiBinder = GWT.create(DocumentsPageUiBinder.class);
	@UiField(provided = true) protected DocumentsMessages msg = null;
	@UiField(provided = true) protected ApplicationMessages globalMsg = null;
	@UiField(provided = true) protected DocumentsResources ressources = null;
	
	@UiField Label instance;
	
	private ApplicationInstanceDTO currentInstance;
	
	interface DocumentsPageUiBinder extends UiBinder<Widget, DocumentsPage> {
	}

	public DocumentsPage() {
		ressources = GWT.create(DocumentsResources.class);		
		ressources.css().ensureInjected();
		msg = GWT.create(DocumentsMessages.class);
		globalMsg = GWT.create(ApplicationMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractDocumentsPagesEvent.TYPE, this);
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractDocumentsPagesEvent.TYPE, this);			
	}
	
	@Override
	public void goBack(Object returnValue) {
		stop();
		EventBus.getInstance().fireEvent(new DocumentsStopEvent());		
		super.goBack(returnValue);
	}
	
	@UiHandler("place")
	void browseAllAvailableGallerie(SelectionChangedEvent event) {
		if (event.getSelection() == 0) {
			NavigationApp app = new NavigationApp();
			app.setTypeApp("kmelia");
			app.setTitle("ECM app browser"); // TODO : i18n
			app.start(this);
		} else if (event.getSelection() == 1) {
			
		}
	}

	@Override
	public void onLoadedSettings(DocumentsLoadedSettingsEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewInstanceLoaded(NewInstanceLoadedEvent event) {
		instance.setText(event.getInstance().getLabel());
		
		// store instance document
		this.currentInstance = event.getInstance();
	}
}