package com.silverpeas.mobile.client.apps.contacts;

import com.silverpeas.mobile.client.apps.contacts.pages.ContactsPage;
import com.silverpeas.mobile.client.common.app.App;

public class ContactsApp extends App {

	public ContactsApp(){
		super();
	}
	
	public void start(){
		setController(new ContactsController());
		setMainPage(new ContactsPage());
		super.start();
	}
}
