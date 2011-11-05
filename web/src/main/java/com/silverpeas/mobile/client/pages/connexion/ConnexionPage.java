package com.silverpeas.mobile.client.pages.connexion;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.DropDownItem;
import com.gwtmobile.ui.client.widgets.DropDownList;
import com.gwtmobile.ui.client.widgets.PasswordTextBox;
import com.gwtmobile.ui.client.widgets.TextBox;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.pages.main.MainPage;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;
import com.silverpeas.mobile.shared.dto.DomainDTO;

public class ConnexionPage extends Page {

	private static ConnexionPageUiBinder uiBinder = GWT.create(ConnexionPageUiBinder.class);
	private MainPage mainPage;
	@UiField(provided = true) protected ApplicationMessages msg = null;
	@UiField(provided = true) protected ApplicationResources res = null;
	@UiField Button go;
	@UiField TextBox loginField;
	@UiField PasswordTextBox passwordField;
	@UiField DropDownList domains;

	interface ConnexionPageUiBinder extends UiBinder<Widget, ConnexionPage> {
	}

	public ConnexionPage() {
		res = GWT.create(ApplicationResources.class);		
		res.css().ensureInjected();
		msg = GWT.create(ApplicationMessages.class);
		
		ServicesLocator.serviceConnection.getDomains(new AsyncCallback<List<DomainDTO>>() {
			public void onFailure(Throwable caught) {
				// TODO : comment afficher l'erreur ?
			}

			public void onSuccess(List<DomainDTO> result) {
				Iterator<DomainDTO> iDomains = result.iterator();
				while (iDomains.hasNext()) {
					DomainDTO domain = iDomains.next();
					DropDownItem item = new DropDownItem();
					item.setValue(domain.getId());
					item.setText(domain.getName());					
					domains.add(item);
				}				
			}
		});
				
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("go")
	void connexion(ClickEvent e) {
		String login = loginField.getText();
		String password = passwordField.getText();

		ServicesLocator.serviceConnection.login(login, password, "1",new AsyncCallback<Void>() {
			public void onFailure(Throwable reason) {
				Window.alert("Loading error");
			}
			public void onSuccess(Void result) {
				mainPage = new MainPage();
				goTo(mainPage);				
			}
		});
	}
}
