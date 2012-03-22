package com.silverpeas.mobile.client.apps.contacts.pages;


import java.util.Iterator;

import com.google.gwt.core.client.GWT;
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
	@UiField ListPanel listPanelContacts;
	@UiField ListPanel listPanelAlphabet;
	@UiField ScrollPanel scrollPanel;
	private int tab[] = new int[26];
	private String id;
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
	
	interface ContactsPageUiBinder extends UiBinder<Widget, ContactsPage> {
	}

	public ContactsPage() {	
		initWidget(uiBinder.createAndBindUi(this));
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
		
		A.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[0]!=0){
					scrollPanel.setScrollPosition(0);
				}
			}
		});
		B.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[1]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(1));
				}
			}
		});
		C.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[2]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(2));
				}
			}
		});
		D.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[3]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(3));
				}
			}
		});
		E.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[4]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(4));
				}
			}
		});
		F.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[5]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(5));
				}
			}
		});
		G.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[6]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(6));
				}
			}
		});
		H.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[7]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(7));
				}
			}
		});
		I.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[8]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(8));
				}
			}
		});
		J.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[9]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(9));
				}
			}
		});
		K.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[10]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(10));
				}
			}
		});
		L.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[11]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(11));
				}
			}
		});
		M.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[12]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(12));
				}
			}
		});
		N.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[13]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(13));
				}
			}
		});
		O.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[14]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(14));
				}
			}
		});
		P.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[15]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(15));
				}
			}
		});
		Q.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[16]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(16));
				}
			}
		});
		R.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[17]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(17));
				}
			}
		});
		S.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[18]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(18));
				}
			}
		});
		T.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[19]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(19));
				}
			}
		});
		U.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[20]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(20));
				}
			}
		});
		V.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[21]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(21));
				}
			}
		});
		W.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[22]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(22));
				}
			}
		});
		X.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[23]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(23));
				}
			}
		});
		Y.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[24]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(24));
				}
			}
		});
		Z.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(tab[25]!=0){
					scrollPanel.setScrollPosition(getIndexOfItem(25));
				}
			}
		});
		
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
		for(int k=0;k<26;k++){
			tab[k]=0;
		}
		while(i.hasNext()){
			DetailUserDTO dudto = i.next();
			ListItem contact = new ListItem();
			String contactName = dudto.getLastName();
			id = dudto.getId();
			Label labelContact = new Label(contactName);
			String lettre = (String) contactName.subSequence(0, 1);
			contact.add(labelContact);
			listPanelContacts.add(contact);
			if(lettre.equals("A")||lettre.equals("a")){
				tab[0]++;
			}
			if(lettre.equals("B")||lettre.equals("b")){
				tab[1]++;
			}
			if(lettre.equals("C")||lettre.equals("c")){
				tab[2]++;
			}
			if(lettre.equals("D")||lettre.equals("d")){
				tab[3]++;
			}
			if(lettre.equals("E")||lettre.equals("e")){
				tab[4]++;
			}
			if(lettre.equals("F")||lettre.equals("f")){
				tab[5]++;
			}
			if(lettre.equals("G")||lettre.equals("g")){
				tab[6]++;
			}
			if(lettre.equals("H")||lettre.equals("h")){
				tab[7]++;
			}
			if(lettre.equals("I")||lettre.equals("i")){
				tab[8]++;
			}
			if(lettre.equals("J")||lettre.equals("j")){
				tab[9]++;
			}
			if(lettre.equals("K")||lettre.equals("k")){
				tab[10]++;
			}
			if(lettre.equals("L")||lettre.equals("l")){
				tab[11]++;
			}
			if(lettre.equals("M")||lettre.equals("m")){
				tab[12]++;
			}
			if(lettre.equals("N")||lettre.equals("n")){
				tab[13]++;
			}
			if(lettre.equals("O")||lettre.equals("o")){
				tab[14]++;
			}
			if(lettre.equals("P")||lettre.equals("p")){
				tab[15]++;
			}
			if(lettre.equals("Q")||lettre.equals("q")){
				tab[16]++;
			}
			if(lettre.equals("R")||lettre.equals("r")){
				tab[17]++;
			}
			if(lettre.equals("S")||lettre.equals("s")){
				tab[18]++;
			}
			if(lettre.equals("T")||lettre.equals("t")){
				tab[19]++;
			}
			if(lettre.equals("U")||lettre.equals("u")){
				tab[20]++;
			}
			if(lettre.equals("V")||lettre.equals("v")){
				tab[21]++;
			}
			if(lettre.equals("W")||lettre.equals("w")){
				tab[22]++;
			}
			if(lettre.equals("X")||lettre.equals("x")){
				tab[23]++;
			}
			if(lettre.equals("Y")||lettre.equals("y")){
				tab[24]++;
			}
			if(lettre.equals("Z")||lettre.equals("z")){
				tab[25]++;
			}
			labelContact.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					ContactDetail contactDetail = new ContactDetail(id);
					goTo(contactDetail);
				}
			});
		}
	}
	
	public int getIndexOfItem(int placeTab){
		int index = 0;
		if(placeTab!=0){
			for(int i = 0;i < placeTab; i++ ){
				index = index + tab[i];
			}
		}
		return index;
	}
}
