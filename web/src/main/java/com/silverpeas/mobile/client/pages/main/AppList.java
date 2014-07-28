package com.silverpeas.mobile.client.pages.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.contacts.ContactsApp;
import com.silverpeas.mobile.client.apps.documents.DocumentsApp;
import com.silverpeas.mobile.client.apps.gallery.GalleryApp;
import com.silverpeas.mobile.client.apps.status.StatusApp;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;

public class AppList extends PageContent {

	private static AppListUiBinder uiBinder = GWT.create(AppListUiBinder.class);

	@UiField(provided = true) protected ApplicationMessages msg = null;
	@UiField(provided = true) protected ApplicationResources res = null;
	@UiField protected Anchor statut, contact, document, media;
	
	interface AppListUiBinder extends UiBinder<Widget, AppList> {
	}

	public AppList() {
		res = GWT.create(ApplicationResources.class);		
		res.css().ensureInjected();
		msg = GWT.create(ApplicationMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	@UiHandler("statut")
	void status(ClickEvent e) {
		App app = new StatusApp();
		app.start();
	}
	
	@UiHandler("contact")
	void contacts(ClickEvent e) {
		App app = new ContactsApp();
		app.start();
	}
	
	@UiHandler("media")
	void gallery(ClickEvent e) {
		//App app = new GalleryApp();
		//app.start();
		//TODO : refactore
	}
	
	@UiHandler("document")
	void documents(ClickEvent e) {		
		App app = new DocumentsApp();
		app.start();
	}

}
