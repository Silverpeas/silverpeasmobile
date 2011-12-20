package com.silverpeas.mobile.client.pages.contacts;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
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
	@UiField DockLayoutPanel dockLayout;
	ListPanel ContactsList = new ListPanel();
	ScrollPanel scrollPanelContact = new ScrollPanel();
	PanelBase panelAlphabet = new PanelBase();
	ListPanel AlphabetList = new ListPanel();
	@UiField HTMLPanel HTMLPanel;
	Label A = new Label("A");
	Label B = new Label("B");
	Label C = new Label("C");
	Label D = new Label("D");
	Label E = new Label("E");
	Label F = new Label("F");
	Label G = new Label("G");
	Label H = new Label("H");
	Label I = new Label("I");
	Label J = new Label("J");
	Label K = new Label("K");
	Label L = new Label("L");
	Label M = new Label("M");
	Label N = new Label("N");
	Label O = new Label("O");
	Label P = new Label("P");
	Label Q = new Label("Q");
	Label R = new Label("R");
	Label S = new Label("S");
	Label T = new Label("T");
	Label U = new Label("U");
	Label V = new Label("V");
	Label W = new Label("W");
	Label X = new Label("X");
	Label Y = new Label("Y");
	Label Z = new Label("Z");
	
	int alphabetListHeight = getScreenHeight();
	
	interface ContactsPageUiBinder extends UiBinder<Widget, ContactsPage> {
	}

	public ContactsPage() {	
		res = GWT.create(ApplicationResources.class);		
		res.css().ensureInjected();
		msg = GWT.create(ApplicationMessages.class);
		initWidget(uiBinder.createAndBindUi(this));

		AlphabetList.add(A);
		AlphabetList.add(B);
		AlphabetList.add(C);
		AlphabetList.add(D);
		AlphabetList.add(E);
		AlphabetList.add(F);
		AlphabetList.add(G);
		AlphabetList.add(H);
		AlphabetList.add(I);
		AlphabetList.add(J);
		AlphabetList.add(K);
		AlphabetList.add(L);
		AlphabetList.add(M);
		AlphabetList.add(N);
		AlphabetList.add(O);
		AlphabetList.add(P);
		AlphabetList.add(Q);
		AlphabetList.add(R);
		AlphabetList.add(S);
		AlphabetList.add(T);
		AlphabetList.add(U);
		AlphabetList.add(V);
		AlphabetList.add(W);
		AlphabetList.add(X);
		AlphabetList.add(Y);
		AlphabetList.add(Z);
		AlphabetList.setHeight(String.valueOf(alphabetListHeight));
		panelAlphabet.add(AlphabetList);
		scrollPanelContact.add(ContactsList);
		dockLayout.addEast(panelAlphabet,10);
		dockLayout.add(scrollPanelContact);
		
		AlphabetList.setVisible(true);
		
		A.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[a|A].*");
			}
		});
		B.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[b|B].*");
			}
		});
		C.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[c|C].*");
			}
		});
		D.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[d|D].*");
			}
		});
		E.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[e|E].*");
			}
		});
		F.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[f|F].*");
			}
		});
		G.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[g|G].*");
			}
		});
		H.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[h|H].*");
			}
		});
		I.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[i|I].*");
			}
		});
		J.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[j|J].*");
			}
		});
		K.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[k|K].*");
			}
		});
		L.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[l|L].*");
			}
		});
		M.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[m|M].*");
			}
		});
		N.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[n|N].*");
			}
		});
		O.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[o|O].*");
			}
		});
		P.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[p|P].*");
			}
		});
		Q.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[q|Q].*");
			}
		});
		R.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[r|R].*");
			}
		});
		S.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[s|S].*");
			}
		});
		T.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[t|T].*");
			}
		});
		U.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[u|U].*");
			}
		});
		V.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[v|V].*");
			}
		});
		W.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[w|W].*");
			}
		});
		X.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[x|X].*");
			}
		});
		Y.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[y|Y].*");
			}
		});
		Z.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadContactsByLetter("^[z|Z].*");
			}
		});
		
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
				scrollPanelContact.setVisible(true);
				scrollPanelContact.add(ContactsList);
			}
		});
	}

	//Résolution de l'écran
	private native int getScreenWidth()/*-{
		return screen.width;
	}-*/;
	
	private native int getScreenHeight()/*-{
		return screen.height;
	}-*/;
}
