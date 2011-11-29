package com.silverpeas.mobile.client.pages.contacts;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.gwtmobile.ui.client.widgets.SlidePanel;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactsPage extends Page {

	private static ContactsPageUiBinder uiBinder = GWT.create(ContactsPageUiBinder.class);
	@UiField(provided = true) protected ApplicationMessages msg = null;
	@UiField(provided = true) protected ApplicationResources res = null;
	@UiField ListPanel ContactsList;
	@UiField SlidePanel slidePanel;

	interface ContactsPageUiBinder extends UiBinder<Widget, ContactsPage> {
	}

	public ContactsPage() {	
		res = GWT.create(ApplicationResources.class);		
		res.css().ensureInjected();
		msg = GWT.create(ApplicationMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
		loadContacts();
	}
	
	public void loadContacts(){
		ServicesLocator.serviceContact.getAllContact(new AsyncCallback<List<DetailUserDTO>>() {
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}
			public void onSuccess(List<DetailUserDTO> result) {
				Iterator<DetailUserDTO> i = result.iterator();
				while(i.hasNext()){
					DetailUserDTO dudto = i.next();
					HorizontalPanel contact = new HorizontalPanel();
					contact.add(new Label(dudto.getLastName()));
					contact.add(new Label(dudto.geteMail()));
					ContactsList.add(contact);
				}
				slidePanel.add(ContactsList);
				slidePanel.setVisible(true);
			}
		});
		
	}
}
