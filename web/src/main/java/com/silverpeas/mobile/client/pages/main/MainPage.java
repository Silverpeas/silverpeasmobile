package com.silverpeas.mobile.client.pages.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.Transition;
import com.silverpeas.mobile.client.apps.contacts.pages.ContactsPage;
import com.silverpeas.mobile.client.apps.dashboard.pages.DashboardPage;
import com.silverpeas.mobile.client.apps.gallery.GalleryApp;
import com.silverpeas.mobile.client.apps.status.pages.StatusPage;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.components.icon.Icon;
import com.silverpeas.mobile.client.pages.agenda.AgendaPage;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;

public class MainPage extends Page {

	private static MainPageUiBinder uiBinder = GWT.create(MainPageUiBinder.class);
	
	private StatusPage statusPage;
	private ContactsPage contactsPage;
	private DashboardPage dashboardPage;
	private AgendaPage agendaPage;
	
	@UiField(provided = true) protected ApplicationMessages msg = null;
	@UiField(provided = true) protected ApplicationResources res = null;
	@UiField protected Icon status, contacts;

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	public MainPage() {
		res = GWT.create(ApplicationResources.class);		
		res.css().ensureInjected();
		msg = GWT.create(ApplicationMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("status")
	void status(ClickEvent e) {
		statusPage = new StatusPage();
		goTo(statusPage, Transition.SLIDEUP);
	}
	
	@UiHandler("contacts")
	void contacts(ClickEvent e) {
		contactsPage = new ContactsPage();
		goTo(contactsPage, Transition.SLIDEUP);
	}
	
	@UiHandler("agenda")
	void agenda(ClickEvent e) {
		agendaPage = new AgendaPage();
		goTo(agendaPage, Transition.SLIDEUP);
	}
	
	@UiHandler("gallery")
	void gallery(ClickEvent e) {
		App app = new GalleryApp();
		app.start(this);
	}
	
	@UiHandler("dashboard")
	void dashboard(ClickEvent e){
		dashboardPage = new DashboardPage();
		goTo(dashboardPage, Transition.SLIDEUP);
	}
}
