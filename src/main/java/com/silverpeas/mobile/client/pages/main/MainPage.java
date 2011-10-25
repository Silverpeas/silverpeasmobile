package com.silverpeas.mobile.client.pages.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.Transition;
import com.silverpeas.mobile.client.components.icon.Icon;
import com.silverpeas.mobile.client.pages.agenda.AgendaPage;
import com.silverpeas.mobile.client.pages.contacts.ContactsPage;
import com.silverpeas.mobile.client.pages.status.StatusPage;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;

public class MainPage extends Page {

	private static MainPageUiBinder uiBinder = GWT.create(MainPageUiBinder.class);
	
	private StatusPage statusPage = new StatusPage();
	private ContactsPage contactsPage = new ContactsPage();
	private AgendaPage agendaPage = new AgendaPage();
	
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
		goTo(statusPage, Transition.FLIP);
	}
	
	@UiHandler("contacts")
	void contacts(ClickEvent e) {
		goTo(contactsPage, Transition.SLIDEUP);
	}
	
	@UiHandler("agenda")
	void agenda(ClickEvent e) {
		goTo(agendaPage, Transition.SLIDE);
	}
}
