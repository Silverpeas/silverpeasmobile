package com.silverpeas.mobile.client.apps.contacts.events.controller;

import com.google.gwt.event.shared.EventHandler;

public interface ContactsControllerEventHandler extends EventHandler{
	void loadContacts(ContactsLoadEvent event);
}
