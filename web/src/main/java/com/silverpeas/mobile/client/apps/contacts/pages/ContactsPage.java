package com.silverpeas.mobile.client.apps.contacts.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.ListItem;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.gwtmobile.ui.client.widgets.ScrollPanel;
import com.gwtmobile.ui.client.widgets.TextBox;
import com.silverpeas.mobile.client.apps.contacts.events.controller.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.AbstractContactsPagesEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactsPage extends Page implements ContactsPagesEventHandler,
		View {

	private static ContactsPageUiBinder uiBinder = GWT.create(ContactsPageUiBinder.class);
	private List<ListItem> contactsList;
	@UiField ListPanel listPanelContacts;
	@UiField ScrollPanel scrollPanel;
	@UiField TextBox textBox;

	interface ContactsPageUiBinder extends UiBinder<Widget, ContactsPage> {
	}

	public ContactsPage() {	
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractContactsPagesEvent.TYPE, this);
		EventBus.getInstance().fireEvent(new ContactsLoadEvent());
		textBox.addKeyUpHandler(new KeyUpHandler() {		
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE && !textBox.getText().isEmpty()) {
					List<ListItem> finalListItem = new ArrayList<ListItem>();
					finalListItem = getFirstItemStartingWith(textBox.getText());
					refresh(finalListItem);
		        }
				else if(textBox.getText().isEmpty() && event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE){
					refresh(contactsList);
				}
			}
		});
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractContactsPagesEvent.TYPE,
				this);
	}

	@Override
	public void onContactsLoaded(ContactsLoadedEvent event) {
		contactsList = new ArrayList<ListItem>();
		Iterator<DetailUserDTO> i = event.getListUserDetailDTO().iterator();
		listPanelContacts.setSelectable(true);
		while (i.hasNext()) {
			DetailUserDTO dudto = i.next();
			ListItem contact = new ListItem();
			final String id = dudto.getId();
			Label labelContact = new Label(dudto.getLastName());
			contact.add(labelContact);

			if(contactsList.size()==0){
				contactsList.add(contact);
			}
			else{
				if(!(contactsList.contains(contact))){
						contactsList.add(contact);
				}
			}
			
			listPanelContacts.add(contact);
			
			labelContact.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					// TODO : afficher fiche contact
					// ContactDetail contactDetail = new ContactDetail(id);
					// goTo(contactDetail);
				}
			});
		}
	}

	public List<ListItem> getFirstItemStartingWith(String search) {
		List<ListItem> listItemStartingWith = new ArrayList<ListItem>();
		for (int i = 0; i < listPanelContacts.getWidgetCount(); i++) {
			ListItem item = listPanelContacts.getItem(i);
			String label = ((Label) item.getWidget(0)).getText().toUpperCase();
			if (label.startsWith(search.toUpperCase())) {
				listItemStartingWith.add(item);
			}
		}
		return listItemStartingWith;
	}
	
	@UiHandler("textBox")
	public void onKeyPress(KeyPressEvent keyPress){
		List<ListItem> finalListItem = new ArrayList<ListItem>();
		if(textBox.getText().isEmpty()){
			finalListItem = getFirstItemStartingWith(keyPress.toString());
		}
		else{
			finalListItem = getFirstItemStartingWith(textBox.getText());
		}
		refresh(finalListItem);
	}
	
	@UiHandler("textBox")
	public void onValueChange(ValueChangeEvent<String> event){
		textBox.setText("");
	}
	
	public void refresh(List<ListItem> listItem){
		listPanelContacts.clear();
		Iterator<ListItem> i = listItem.iterator();
		while(i.hasNext()){
			ListItem listItemTemp = i.next();
			listPanelContacts.add(listItemTemp);
		}
	}
}