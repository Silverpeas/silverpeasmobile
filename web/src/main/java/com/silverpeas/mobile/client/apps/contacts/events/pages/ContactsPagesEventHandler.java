package com.silverpeas.mobile.client.apps.contacts.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface ContactsPagesEventHandler extends EventHandler{
	void onContactsLoaded(ContactsLoadedEvent event);
}
