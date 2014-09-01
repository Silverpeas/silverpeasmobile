package com.silverpeas.mobile.client.pages.connexion;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.pages.main.AppList;
import com.silverpeas.mobile.client.persist.User;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.DomainDTO;


public class ConnexionPage extends PageContent {

	private static ConnexionPageUiBinder uiBinder = GWT.create(ConnexionPageUiBinder.class);

	@UiField(provided = true) protected ApplicationMessages msg = null;	
	@UiField Anchor go;
	@UiField TextBox loginField;
	@UiField PasswordTextBox passwordField;
	@UiField ListBox domains;

	interface ConnexionPageUiBinder extends UiBinder<Widget, ConnexionPage> {
	}

	public ConnexionPage() {
		msg = GWT.create(ApplicationMessages.class);
		loadDomains();
		initWidget(uiBinder.createAndBindUi(this));
		loginField.getElement().setId("Login");
		passwordField.getElement().setId("Password");
		domains.getElement().setId("DomainId");
	}

	/**
	 * Gestion du clique sur le bouton go.
	 * @param e
	 */
	@UiHandler("go")
	void connexion(ClickEvent e) {
		clickGesture(new Command() {
			@Override
			public void execute() {
				String login = loginField.getText();
				String password = passwordField.getText();
				login(login, password, domains.getValue(domains.getSelectedIndex()));
			}
		});
	}

	/**
	 * Transition de la demande de connexion au serveur.
	 * @param login
	 * @param password
	 * @param domainId
	 */
	private void login(String login, final String password, String domainId) {
		if (!login.isEmpty() && !password.isEmpty()) {
			ServicesLocator.serviceConnection.login(login, password, domainId, new AsyncCallback<DetailUserDTO>() {
				public void onFailure(Throwable caught) {
					EventBus.getInstance().fireEvent(new ErrorEvent(caught));
				}
				public void onSuccess(DetailUserDTO user) {
					String encryptedPassword = null;
					try {
						encryptedPassword = encryptPassword(password);
					} catch (InvalidCipherTextException e) {
						EventBus.getInstance().fireEvent(new ErrorEvent(e));
					}
					SpMobil.user = user;
					storeIds(encryptedPassword);
					
					RootPanel.get().clear();
					RootPanel.get().add(SpMobil.mainPage);
					PageHistory.getInstance().goTo(new AppList());
				}
			});	
		}
	}

	private String encryptPassword(String password) throws InvalidCipherTextException {
		TripleDesCipher cipher = new TripleDesCipher();
		cipher.setKey(SpMobil.configuration.getDESKey().getBytes());
		String encryptedPassword = cipher.encrypt(passwordField.getText());
		return encryptedPassword;
	}
	
	/**
	 * Mémorisation des identifiants de l'utilisateur.
	 */
	private void storeIds(final String encryptedPassword) {
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {
			User user = new User(loginField.getText(), encryptedPassword, domains.getValue(domains.getSelectedIndex()));
			storage.setItem("userConnected", user.toJson());
		}
	}
	
	/**
	 * Récupération de la liste des domaines.
	 */
	private void loadDomains() {
		ServicesLocator.serviceConnection.getDomains(new AsyncCallback<List<DomainDTO>>() {
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}

			public void onSuccess(List<DomainDTO> result) {
				Iterator<DomainDTO> iDomains = result.iterator();
				while (iDomains.hasNext()) {
					DomainDTO domain = iDomains.next();
					domains.addItem(domain.getName(), domain.getId());
				}
			}
		});
	}

}
