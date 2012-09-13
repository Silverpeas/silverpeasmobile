package com.silverpeas.mobile.client.apps.contacts.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.ListItem;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.gwtmobile.ui.client.widgets.RadioButton;
import com.gwtmobile.ui.client.widgets.RadioButtonGroup;
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
	@UiField RadioButtonGroup group;
	@UiField RadioButton all, my;

	interface ContactsPageUiBinder extends UiBinder<Widget, ContactsPage> {
	}

	public ContactsPage() {	
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractContactsPagesEvent.TYPE, this);
		EventBus.getInstance().fireEvent(new ContactsLoadEvent("MY"));
		my.setValue(true);
		all.setValue(false);
		textBox.addKeyUpHandler(new KeyUpHandler() {		
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(textBox.getText().isEmpty()){
					refresh(contactsList);
				}
				else{
					List<ListItem> finalListItem = new ArrayList<ListItem>();
					finalListItem = getFirstItemStartingWith(textBox.getText());
					refresh(finalListItem);
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
		listPanelContacts.clear();
		contactsList = new ArrayList<ListItem>();
		listPanelContacts.setSelectable(true);
		Iterator<DetailUserDTO> i = event.getListUserDetailDTO().iterator();
		while (i.hasNext()) {
			DetailUserDTO dudto = i.next();
			ListItem contact = new ListItem();
			final String id = dudto.getId();
			Label labelContact = new Label(dudto.getLastName());
			contact.add(labelContact);
			contactsList.add(contact);
			listPanelContacts.add(contact);
			labelContact.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					ContactDetail contactDetail = new ContactDetail(id);
					goTo(contactDetail);
				}
			});
		}
	}

	public List<ListItem> getFirstItemStartingWith(String search) {
		List<ListItem> listItemStartingWith = new ArrayList<ListItem>();
		for (int i = 0; i < contactsList.size(); i++) {
			ListItem item = contactsList.get(i);
			String label = ((Label) item.getWidget(0)).getText().toUpperCase();
			if (label.startsWith(search.toUpperCase())) {
				listItemStartingWith.add(item);
			}
		}
		return listItemStartingWith;
	}

	@UiHandler("group")
    void onRadioGroupSelectionChanged(SelectionChangedEvent e) {
    	RadioButton radio = (RadioButton) group.getWidget(e.getSelection());
    	if(radio.getText().equals("All Contacts")){
    		EventBus.getInstance().fireEvent(new ContactsLoadEvent("ALL"));
    	}
    	else{
    		EventBus.getInstance().fireEvent(new ContactsLoadEvent("MY"));
    	}
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