package com.silverpeas.mobile.client.apps.contacts;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.contacts.events.app.AbstractContactsAppEvent;
import com.silverpeas.mobile.client.apps.contacts.events.app.ContactsAppEventHandler;
import com.silverpeas.mobile.client.apps.contacts.events.app.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.pages.ContactsPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactsApp extends App implements ContactsAppEventHandler {

	public ContactsApp(){
		super();
	}
	
	public void start(){
		EventBus.getInstance().addHandler(AbstractContactsAppEvent.TYPE, this);
		setMainPage(new ContactsPage());		
		super.start();
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractContactsAppEvent.TYPE, this);
		super.stop();
	}

	@Override
	public void loadContacts(ContactsLoadEvent event) {
		Notification.activityStart();
		ServicesLocator.serviceContact.getContacts(event.getFilter(), new AsyncCallback<List<DetailUserDTO>>() {
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}
			public void onSuccess(List<DetailUserDTO> result) {
				EventBus.getInstance().fireEvent(new ContactsLoadedEvent(result));
				Notification.activityStop();
			}
		});		
	}
}
