package com.silverpeas.mobile.client.apps.contacts.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.HorizontalPanel;
import com.silverpeas.mobile.client.apps.contacts.events.controller.AddContactEvent;
import com.silverpeas.mobile.client.apps.contacts.events.controller.ContactDetailLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.AbstractContactsDetailPagesEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactDetailLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactDetailPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.PageView;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactDetail extends PageView implements ContactDetailPagesEventHandler, View{

	private static ContactDetailUiBinder uiBinder = GWT.create(ContactDetailUiBinder.class);
	private DetailUserDTO detailUserDTO;
	private Image image;
	@UiField Label lastName;
	@UiField Anchor eMail;
	@UiField Label firstName;
	@UiField Anchor phoneNumber;
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
		avatarPanel.clear();
		detailUserDTO = event.getUserDetailDTO();
		image = new Image(detailUserDTO.getAvatar());
		avatarPanel.add(image);
		lastName.setText(detailUserDTO.getLastName());
		firstName.setText(detailUserDTO.getFirstName());
		eMail.setText(detailUserDTO.geteMail());
		eMail.setHref("mailto:"+detailUserDTO.geteMail());
		phoneNumber.setText(detailUserDTO.getPhoneNumber());
		phoneNumber.setHref("tel:"+detailUserDTO.getPhoneNumber());
	}
	
	@UiHandler("addButton")
	public void addContact(ClickEvent e){
		clickGesture(new Command() {			
			@Override
			public void execute() {
				EventBus.getInstance().fireEvent(new AddContactEvent(detailUserDTO));				
			}
		});
	}
}
