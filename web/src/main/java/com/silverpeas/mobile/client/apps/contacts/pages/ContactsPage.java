package com.silverpeas.mobile.client.apps.contacts.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.contacts.events.controller.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.AbstractContactsPagesEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsPagesEventHandler;
import com.silverpeas.mobile.client.apps.contacts.resources.ContactsMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.contact.ContactFilters;

public class ContactsPage extends PageContent implements ContactsPagesEventHandler, View {

	private static ContactsPageUiBinder uiBinder = GWT.create(ContactsPageUiBinder.class);
	
	@UiField(provided = true) protected ContactsMessages msg = null;
	@UiField HTMLPanel container;
	@UiField Anchor mycontacts, allcontacts;
	
	interface ContactsPageUiBinder extends UiBinder<Widget, ContactsPage> {
	}

	public ContactsPage() {	
		msg = GWT.create(ContactsMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
		container.getElement().setId("contacts");
		mycontacts.getElement().setId("btn-my-contacts");
		allcontacts.getElement().setId("btn-all-contacts");
		EventBus.getInstance().addHandler(AbstractContactsPagesEvent.TYPE, this);
		EventBus.getInstance().fireEvent(new ContactsLoadEvent(ContactFilters.MY));		
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractContactsPagesEvent.TYPE, this);
	}

	@Override
	public void onContactsLoaded(ContactsLoadedEvent event) {
		/*listPanelContacts.clear();
		contactsList = new ArrayList<ListItem>();
		listPanelContacts.setSelectable(true);
		Iterator<DetailUserDTO> i = event.getListUserDetailDTO().iterator();
		while (i.hasNext()) {
			DetailUserDTO dudto = i.next();
			ListItem contact = new ListItem();
			final String id = dudto.getId();
			Label labelContact = new Label(dudto.getLastName());
			labelContact.setWidth("100%");
			contact.add(labelContact);
			contactsList.add(contact);
			listPanelContacts.add(contact);
			labelContact.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					clickGesture(new Command() {						
						@Override
						public void execute() {
							ContactDetail contactDetail = new ContactDetail(id);
							goTo(contactDetail);							
						}
					});
				}
			});
		}*/
	}

	/*public List<ListItem> getFirstItemStartingWith(String search) {
		List<ListItem> listItemStartingWith = new ArrayList<ListItem>();
		for (int i = 0; i < contactsList.size(); i++) {
			ListItem item = contactsList.get(i);
			String label = ((Label) item.getWidget(0)).getText().toUpperCase();
			if (label.startsWith(search.toUpperCase())) {
				listItemStartingWith.add(item);
			}
		}
		return listItemStartingWith;
	}*/

	/*@UiHandler("group")
    void onRadioGroupSelectionChanged(SelectionChangedEvent e) {
		
    	if(e.getSelection() == 1){
    		EventBus.getInstance().fireEvent(new ContactsLoadEvent(ContactFilters.ALL));
    	}
    	else{
    		EventBus.getInstance().fireEvent(new ContactsLoadEvent(ContactFilters.MY));
    	}
    }*/
	
	/*public void refresh(List<ListItem> listItem){
		listPanelContacts.clear();
		Iterator<ListItem> i = listItem.iterator();
		while(i.hasNext()){
			ListItem listItemTemp = i.next();
			listPanelContacts.add(listItemTemp);
		}
	}*/
}