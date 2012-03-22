package com.silverpeas.mobile.client.apps.contacts;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.contacts.events.controller.AbstractContactsControllerEvent;
import com.silverpeas.mobile.client.apps.contacts.events.controller.ContactsControllerEventHandler;
import com.silverpeas.mobile.client.apps.contacts.events.controller.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactsController implements Controller, ContactsControllerEventHandler{
	
	public ContactsController(){
		super();
		EventBus.getInstance().addHandler(AbstractContactsControllerEvent.TYPE, this);
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractContactsControllerEvent.TYPE, this);
	}

	@Override
	public void loadContacts(ContactsLoadEvent event) {
		ServicesLocator.serviceContact.getAllContact(new AsyncCallback<List<DetailUserDTO>>() {
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}
			public void onSuccess(List<DetailUserDTO> result) {
				EventBus.getInstance().fireEvent(new ContactsLoadedEvent(result));
			}
		});
	}
}
