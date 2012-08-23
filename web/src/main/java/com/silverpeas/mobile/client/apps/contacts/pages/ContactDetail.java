package com.silverpeas.mobile.client.apps.contacts.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.HorizontalPanel;
import com.silverpeas.mobile.client.apps.contacts.events.controller.AddContactEvent;
import com.silverpeas.mobile.client.apps.contacts.events.controller.CallContactEvent;
import com.silverpeas.mobile.client.apps.contacts.events.controller.ContactDetailLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.AbstractContactsDetailPagesEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactDetailLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactDetailPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactDetail extends Page implements ContactDetailPagesEventHandler, View{

	private static ContactDetailUiBinder uiBinder = GWT.create(ContactDetailUiBinder.class);
	private DetailUserDTO detailUserDTO;
	private Image image;
	@UiField Label lastName;
	@UiField Label eMail;
	@UiField Label firstName;
	@UiField Label phoneNumber;
	@UiField Button callButton;
	@UiField Button addButton;
	@UiField HorizontalPanel avatarPanel;
	
	interface ContactDetailUiBinder extends UiBinder<Widget, ContactDetail> {
	}
	
	public ContactDetail(String id){
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractContactsDetailPagesEvent.TYPE, this);
		EventBus.getInstance().fireEvent(new ContactDetailLoadEvent(id));
	}
	
	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractContactsDetailPagesEvent.TYPE, this);
	}

	@Override
	public void onContactDetailLoaded(ContactDetailLoadedEvent event) {
		detailUserDTO = event.getUserDetailDTO();
		image = new Image(detailUserDTO.getAvatar());
		avatarPanel.add(image);
		lastName.setText("Last Name : "+detailUserDTO.getLastName());
		firstName.setText("First Name : "+detailUserDTO.getFirstName());
		eMail.setText("Email : "+detailUserDTO.geteMail());
		phoneNumber.setText("Numéro de téléphone : "+detailUserDTO.getPhoneNumber());
	}
	
	@UiHandler("callButton")
	public void callContact(ClickEvent e){
		EventBus.getInstance().fireEvent(new CallContactEvent());
	}
	
	@UiHandler("addButton")
	public void addContact(ClickEvent e){
		EventBus.getInstance().fireEvent(new AddContactEvent(detailUserDTO));
	}
}
