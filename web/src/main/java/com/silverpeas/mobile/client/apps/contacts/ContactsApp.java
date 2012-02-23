package com.silverpeas.mobile.client.apps.contacts;

import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.contacts.events.app.internal.AbstractContactEvent;
import com.silverpeas.mobile.client.apps.contacts.events.app.internal.ContactsEventHandler;
import com.silverpeas.mobile.client.apps.contacts.events.app.internal.ContactsStopEvent;
import com.silverpeas.mobile.client.apps.contacts.pages.ContactsPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;

public class ContactsApp extends App implements ContactsEventHandler{

	public ContactsApp(){
		super();
		EventBus.getInstance().addHandler(AbstractContactEvent.TYPE, this);
	}
	
	public void Start(Page lauchingPage){
		setController(new ContactsController());
		setMainPage(new ContactsPage());
		super.start(lauchingPage);
	}
	
	@Override
	public void stop(){
		EventBus.getInstance().removeHandler(AbstractContactEvent.TYPE, this);
		super.stop();
	}
	
	@Override
	public void onStop(ContactsStopEvent event) {
		stop();
	}
}
