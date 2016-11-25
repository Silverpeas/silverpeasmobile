package com.silverpeas.mobile.client.apps.contacts.events.app;

import com.google.gwt.event.shared.EventHandler;

public interface ContactsAppEventHandler extends EventHandler{
	void loadContacts(ContactsLoadEvent event);
}
