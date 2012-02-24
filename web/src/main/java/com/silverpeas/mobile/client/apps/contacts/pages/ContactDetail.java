package com.silverpeas.mobile.client.apps.contacts.pages;

import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.contacts.events.pages.AbstractContactsDetailPagesEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactDetailLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactDetailPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;

public class ContactDetail extends Page implements ContactDetailPagesEventHandler, View{

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractContactsDetailPagesEvent.TYPE, this);
	}

	@Override
	public void onContactDetailLoaded(ContactDetailLoadedEvent event) {
		// TODO Auto-generated method stub
		
	}
}
