package com.silverpeas.mobile.client.apps.contacts.pages;


import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.ListItem;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.gwtmobile.ui.client.widgets.ScrollPanel;
import com.silverpeas.mobile.client.apps.contacts.events.controller.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.AbstractContactsPagesEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactsPage extends Page implements ContactsPagesEventHandler, View{

	private static ContactsPageUiBinder uiBinder = GWT.create(ContactsPageUiBinder.class);
	private static final String ALPHABET ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	@UiField ListPanel listPanelContacts;
	@UiField ListPanel listPanelAlphabet;
	@UiField ScrollPanel scrollPanel;
	
	interface ContactsPageUiBinder extends UiBinder<Widget, ContactsPage> {
	}

	public ContactsPage() {	
		initWidget(uiBinder.createAndBindUi(this));
		
		for (int i = 0; i < ALPHABET.length(); i++) {
			final char letter = ALPHABET.charAt(i);			
			Label letterW = new Label(String.valueOf(letter));			
			
			letterW.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {				
					ListItem item = getFisrtItemStartingWith(String.valueOf(letter));
					if (item != null) {					
						scrollPanel.setPostionToTop();					
						scrollPanel.setScrollPosition((item.getElement().getAbsoluteTop() - item.getElement().getOffsetHeight() ) * -1);					
						for (int i = 0; i < listPanelAlphabet.getWidgetCount(); i++) {
							ListItem it = listPanelAlphabet.getItem(i);			
							it.getWidget(0).getElement().getStyle().clearFontSize();
						}					
						((Label) event.getSource()).getElement().getStyle().setFontSize(2, Unit.EM);
					}				
			}});			
			listPanelAlphabet.add(letterW);
		}		
		EventBus.getInstance().addHandler(AbstractContactsPagesEvent.TYPE, this);
		EventBus.getInstance().fireEvent(new ContactsLoadEvent());
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractContactsPagesEvent.TYPE, this);	
	}

	@Override
	public void onContactsLoaded(ContactsLoadedEvent event) {
		Iterator<DetailUserDTO> i = event.getListUserDetailDTO().iterator();
		listPanelContacts.setSelectable(true);
		while(i.hasNext()){
			DetailUserDTO dudto = i.next();
			ListItem contact = new ListItem();
			final String id = dudto.getId();
			Label labelContact = new Label(dudto.getLastName());
			contact.add(labelContact);
			
			listPanelContacts.add(contact);			
			labelContact.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					//TODO : afficher fiche contact
					//ContactDetail contactDetail = new ContactDetail(id);
					//goTo(contactDetail);
				}
			});
		}
	}
	
	public ListItem getFisrtItemStartingWith(String letter){
		for (int i = 0; i < listPanelContacts.getWidgetCount(); i++) {
			ListItem item = listPanelContacts.getItem(i);			
			String label = ((Label) item.getWidget(0)).getText().toUpperCase();
			if (label.startsWith(letter)) {
				return item;
			}			
		}
		return null;
	}
}
