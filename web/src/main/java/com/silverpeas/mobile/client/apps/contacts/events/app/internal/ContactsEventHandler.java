package com.silverpeas.mobile.client.apps.contacts.events.app.internal;

import com.google.gwt.event.shared.EventHandler;

public interface ContactsEventHandler extends EventHandler{
	void onStop(ContactsStopEvent event);
}
