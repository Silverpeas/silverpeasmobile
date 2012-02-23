package com.silverpeas.mobile.client.apps.contacts.pages;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.ListItem;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.gwtmobile.ui.client.widgets.ScrollPanel;
import com.silverpeas.mobile.client.apps.contacts.events.controller.ContactsLoadByLetterEvent;
import com.silverpeas.mobile.client.apps.contacts.events.controller.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.AbstractContactsPagesEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsByLetterLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactsPage extends Page implements ContactsPagesEventHandler, View{

	private static ContactsPageUiBinder uiBinder = GWT.create(ContactsPageUiBinder.class);
	/*@UiField ListPanel listPanelContacts;
	@UiField ListPanel listPanelAlphabet;
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
	@UiField Label Z;*/
	@UiField DockLayoutPanel dockLayoutPanel;
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
	ListPanel listPanelContacts = new ListPanel();
	ScrollPanel scrollPanelContacts = new ScrollPanel();
	ListPanel listPanelAlphabet = new ListPanel();
	
	
	String alphabetListHeight = String.valueOf(getScreenHeight());
	int alphabetListWidth = getScreenWidth();
	
	interface ContactsPageUiBinder extends UiBinder<Widget, ContactsPage> {
	}

	public ContactsPage() {	
		initWidget(uiBinder.createAndBindUi(this));
		scrollPanelContacts.add(listPanelContacts);
		dockLayoutPanel.addWest(scrollPanelContacts, 10);
		listPanelAlphabet.add(A);
		listPanelAlphabet.add(B);
		listPanelAlphabet.add(C);
		listPanelAlphabet.add(D);
		listPanelAlphabet.add(E);
		listPanelAlphabet.add(F);
		listPanelAlphabet.add(G);
		listPanelAlphabet.add(H);
		listPanelAlphabet.add(I);
		listPanelAlphabet.add(J);
		listPanelAlphabet.add(K);
		listPanelAlphabet.add(L);
		listPanelAlphabet.add(M);
		listPanelAlphabet.add(N);
		listPanelAlphabet.add(O);
		listPanelAlphabet.add(P);
		listPanelAlphabet.add(Q);
		listPanelAlphabet.add(R);
		listPanelAlphabet.add(S);
		listPanelAlphabet.add(T);
		listPanelAlphabet.add(U);
		listPanelAlphabet.add(V);
		listPanelAlphabet.add(W);
		listPanelAlphabet.add(X);
		listPanelAlphabet.add(Y);
		listPanelAlphabet.add(Z);
		dockLayoutPanel.add(listPanelAlphabet);
		
		A.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[a|A].*"));
			}
		});
		B.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[b|B].*"));
			}
		});
		C.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[c|C].*"));
			}
		});
		D.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[d|D].*"));
			}
		});
		E.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[e|E].*"));
			}
		});
		F.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[f|F].*"));
			}
		});
		G.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[g|G].*"));
			}
		});
		H.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[h|H].*"));
			}
		});
		I.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[i|I].*"));
			}
		});
		J.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[j|J].*"));
			}
		});
		K.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[k|K].*"));
			}
		});
		L.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[l|L].*"));
			}
		});
		M.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[m|M].*"));
			}
		});
		N.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[n|N].*"));
			}
		});
		O.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[o|O].*"));
			}
		});
		P.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[p|P].*"));
			}
		});
		Q.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[q|Q].*"));
			}
		});
		R.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[r|R].*"));
			}
		});
		S.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[s|S].*"));
			}
		});
		T.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[t|T].*"));
			}
		});
		U.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[u|U].*"));
			}
		});
		V.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[v|V].*"));
			}
		});
		W.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[w|W].*"));
			}
		});
		X.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[x|X].*"));
			}
		});
		Y.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[y|Y].*"));
			}
		});
		Z.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(new ContactsLoadByLetterEvent("^[z|Z].*"));
			}
		});
		
		EventBus.getInstance().addHandler(AbstractContactsPagesEvent.TYPE, this);
		EventBus.getInstance().fireEvent(new ContactsLoadEvent());
	}

	//Résolution de l'écran
	private native int getScreenWidth()/*-{
		return screen.width;
	}-*/;
	
	private native int getScreenHeight()/*-{
		return screen.height;
	}-*/;

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractContactsPagesEvent.TYPE, this);	
	}

	@Override
	public void onContactsLoaded(ContactsLoadedEvent event) {

	}

	@Override
	public void onContactsByLetterLoaded(ContactsByLetterLoadedEvent event) {
		Iterator<DetailUserDTO> i = event.getDetailUserDTO().iterator();
		while(i.hasNext()){
			DetailUserDTO dudto = i.next();
			ListItem contact = new ListItem();
			contact.add(new Label(dudto.getLastName()));
			listPanelContacts.add(contact);
		}
	}
}
