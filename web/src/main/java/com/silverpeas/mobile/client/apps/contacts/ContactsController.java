package com.silverpeas.mobile.client.apps.contacts;

import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmobile.phonegap.client.Contacts;
import com.gwtmobile.phonegap.client.Contacts.Callback;
import com.gwtmobile.phonegap.client.Contacts.ContactError;
import com.silverpeas.mobile.client.apps.contacts.events.controller.AbstractContactsControllerEvent;
import com.silverpeas.mobile.client.apps.contacts.events.controller.AddContactEvent;
import com.silverpeas.mobile.client.apps.contacts.events.controller.ContactDetailLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.controller.ContactsControllerEventHandler;
import com.silverpeas.mobile.client.apps.contacts.events.controller.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactDetailLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.phonegap.PhoneContact;
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

	@Override
	public void loadContactDetail(ContactDetailLoadEvent event) {
		Notification.activityStart();
		ServicesLocator.serviceContact.getContactDetail(event.getId(), new AsyncCallback<DetailUserDTO>(){
			public void onFailure(Throwable caught) {				
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}
			public void onSuccess(DetailUserDTO result) {				
				EventBus.getInstance().fireEvent(new ContactDetailLoadedEvent(result));
				Notification.activityStop();
			}
		});
	}

	@Override
	public void addContact(AddContactEvent event) {
		DetailUserDTO detailUserDTO = event.getUserDetailDTO();
		final PhoneContact contact = (PhoneContact) Contacts.newInstance();
		contact.setDisplayName(detailUserDTO.getLastName());
		contact.setPhonenUmber(detailUserDTO.getPhoneNumber());
		contact.save(new Callback() {			
			@Override
			public void onSuccess() {				
				//TODO : send event to page for display message
				Window.alert("Contact successfully added.");
			}			
			@Override
			public void onError(ContactError error) {
				//TODO : send event to page for display message
				Window.alert("Contact creation failed.");
			}
		});
	}
}