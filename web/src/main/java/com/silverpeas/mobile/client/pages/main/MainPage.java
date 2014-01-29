package com.silverpeas.mobile.client.pages.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.HeaderPanel;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.almanach.AlmanachApp;
import com.silverpeas.mobile.client.apps.contacts.ContactsApp;
import com.silverpeas.mobile.client.apps.dashboard.DashboardApp;
import com.silverpeas.mobile.client.apps.documents.DocumentsApp;
import com.silverpeas.mobile.client.apps.gallery.GalleryApp;
import com.silverpeas.mobile.client.apps.status.StatusApp;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.app.PageView;
import com.silverpeas.mobile.client.components.icon.Icon;
import com.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;

public class MainPage extends PageView {

	private static MainPageUiBinder uiBinder = GWT.create(MainPageUiBinder.class);
	
	@UiField(provided = true) protected ApplicationMessages msg = null;
	@UiField(provided = true) protected ApplicationResources res = null;
	@UiField protected Icon status, contacts;
	@UiField HeaderPanel header;

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	public MainPage() {
		res = GWT.create(ApplicationResources.class);		
		res.css().ensureInjected();
		msg = GWT.create(ApplicationMessages.class);
		initWidget(uiBinder.createAndBindUi(this));		
		header.getRightButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {				
				new SpMobil().clearIds();
				ConnexionPage connexionPage = new ConnexionPage();
				Page.load(connexionPage);
			}
		});
	}
	
	@UiHandler("status")
	void status(ClickEvent e) {
		App app = new StatusApp();
		app.start(this);
	}
	
	@UiHandler("contacts")
	void contacts(ClickEvent e) {
		App app = new ContactsApp();
		app.start(this);
	}
	
	@UiHandler("almanach")
	void Almanach(ClickEvent e) {
		App app = new AlmanachApp();
		app.start(this);
	}
	
	@UiHandler("gallery")
	void gallery(ClickEvent e) {
		App app = new GalleryApp();
		app.start(this);
	}
	
	@UiHandler("documents")
	void documents(ClickEvent e) {
		App app = new DocumentsApp();
		app.start(this);
	}
	
	@UiHandler("dashboard")
	void dashboard(ClickEvent e){
		App app = new DashboardApp();
		app.start(this);
	}
}
