package com.silverpeas.mobile.client.pages.contacts;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.gwtmobile.ui.client.widgets.PanelBase;
import com.gwtmobile.ui.client.widgets.ScrollPanel;
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
	@UiField ScrollPanel scrollPanelContact;
	@UiField PanelBase panelAlphabet;
	@UiField ListPanel AlphabetList;
	@UiField Label A;
	@UiField Label B;
	@UiField Label C;
	@UiField Label D;
	@UiField Label E;
	@UiField Label F;
	@UiField Label G;
	@UiField Label H;
	@UiField Label I;
	@UiField Label J;
	@UiField Label K;
	@UiField Label L;
	@UiField Label M;
	@UiField Label N;
	@UiField Label O;
	@UiField Label P;
	@UiField Label Q;
	@UiField Label R;
	@UiField Label S;
	@UiField Label T;
	@UiField Label U;
	@UiField Label V;
	@UiField Label W;
	@UiField Label X;
	@UiField Label Y;
	@UiField Label Z;

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
		ServicesLocator.serviceContact.getAllContact(new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}
			public void onSuccess(Void result) {
				
			}
		});
		scrollPanelContact.setVisible(true);
		panelAlphabet.setVisible(true);
	}
	
	public void loadContactsByLetter(String letter){
		ServicesLocator.serviceContact.getContactsByLetter(letter, new AsyncCallback<List<DetailUserDTO>>(){
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}
			public void onSuccess(List<DetailUserDTO> result) {
				Iterator<DetailUserDTO> i = result.iterator();
				while(i.hasNext()){
					DetailUserDTO dudto = i.next();
					HorizontalPanel contact = new HorizontalPanel();
					contact.add(new Label(dudto.getLastName()));
					ContactsList.add(contact);
				}
				scrollPanelContact.add(ContactsList);
				scrollPanelContact.setVisible(true);
			}
		});
	}
	
	@UiHandler("A")
	void a(ClickEvent e) {
		loadContactsByLetter("^[a|A].*");
	}
	@UiHandler("B")
	void b(ClickEvent e) {
		loadContactsByLetter("^[b|B].*");
	}
	@UiHandler("C")
	void c(ClickEvent e) {
		loadContactsByLetter("^[c|C].*");
	}
	@UiHandler("D")
	void d(ClickEvent e) {
		loadContactsByLetter("^[d|D].*");
	}
	@UiHandler("E")
	void e(ClickEvent e) {
		loadContactsByLetter("^[e|E].*");
	}
	@UiHandler("F")
	void f(ClickEvent e) {
		loadContactsByLetter("^[f|F].*");
	}
	@UiHandler("G")
	void g(ClickEvent e) {
		loadContactsByLetter("^[g|G].*");
	}
	@UiHandler("H")
	void h(ClickEvent e) {
		loadContactsByLetter("^[h|H].*");
	}
	@UiHandler("I")
	void i(ClickEvent e) {
		loadContactsByLetter("^[i|I].*");
	}
	@UiHandler("J")
	void j(ClickEvent e) {
		loadContactsByLetter("^[j|J].*");
	}
	@UiHandler("K")
	void k(ClickEvent e) {
		loadContactsByLetter("^[k|K].*");
	}
	@UiHandler("L")
	void l(ClickEvent e) {
		loadContactsByLetter("^[l|L].*");
	}
	@UiHandler("M")
	void m(ClickEvent e) {
		loadContactsByLetter("^[m|M].*");
	}
	@UiHandler("N")
	void n(ClickEvent e) {
		loadContactsByLetter("^[n|N].*");
	}
	@UiHandler("O")
	void o(ClickEvent e) {
		loadContactsByLetter("^[o|O].*");
	}
	@UiHandler("P")
	void p(ClickEvent e) {
		loadContactsByLetter("^[p|P].*");
	}
	@UiHandler("Q")
	void q(ClickEvent e) {
		loadContactsByLetter("^[q|Q].*");
	}
	@UiHandler("R")
	void r(ClickEvent e) {
		loadContactsByLetter("^[r|R].*");
	}
	@UiHandler("S")
	void s(ClickEvent e) {
		loadContactsByLetter("^[s|S].*");
	}
	@UiHandler("T")
	void t(ClickEvent e) {
		loadContactsByLetter("^[t|T].*");
	}
	@UiHandler("U")
	void u(ClickEvent e) {
		loadContactsByLetter("^[u|U].*");
	}
	@UiHandler("V")
	void v(ClickEvent e) {
		loadContactsByLetter("^[v|V].*");
	}
	@UiHandler("W")
	void w(ClickEvent e) {
		loadContactsByLetter("^[w|W].*");
	}
	@UiHandler("X")
	void x(ClickEvent e) {
		loadContactsByLetter("^[x|X].*");
	}
	@UiHandler("Y")
	void y(ClickEvent e) {
		loadContactsByLetter("^[y|Y].*");
	}
	@UiHandler("Z")
	void z(ClickEvent e) {
		loadContactsByLetter("^[z|Z].*");
	}
}
