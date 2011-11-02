package com.silverpeas.mobile.client.pages.contacts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.gwtmobile.ui.client.widgets.SlidePanel;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;

public class ContactsPage extends Page {

	private static ContactsPageUiBinder uiBinder = GWT.create(ContactsPageUiBinder.class);
	@UiField(provided = true) protected ApplicationMessages msg = null;
	@UiField(provided = true) protected ApplicationResources res = null;
	
	@UiField ListPanel aContactsList, bContactsList;
	@UiField SlidePanel slidePanel;

	interface ContactsPageUiBinder extends UiBinder<Widget, ContactsPage> {
	}

	public ContactsPage() {	
		/*res = GWT.create(ApplicationResources.class);		
		res.css().ensureInjected();
		msg = GWT.create(ApplicationMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
		
		for (int i = 0; i < 25; i++) {
			HorizontalPanel contact = new HorizontalPanel();
			contact.setSpacing(10);
			contact.add(new Image(res.contacts()));
			contact.add(new Label("A n°"+i));
			contact.add(new Image(res.online()));
			aContactsList.add(contact);			
		}
		for (int i = 0; i < 15; i++) {
			HorizontalPanel contact = new HorizontalPanel();
			contact.setSpacing(10);
			contact.add(new Image(res.contacts()));
			contact.add(new Label("B n°"+i));
			contact.add(new Image(res.offline()));
			bContactsList.add(contact);			
		}
		slidePanel.setVisible(true);*/
		String id = new String("");
		ServicesLocator.serviceContact.ContactList(id, new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Window.alert("Update status failed.");
            }
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				Window.alert("Update status done.");
				goBack(null);
			}
		});
	}
}
