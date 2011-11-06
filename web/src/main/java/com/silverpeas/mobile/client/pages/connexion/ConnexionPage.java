package com.silverpeas.mobile.client.pages.connexion;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.persistence.client.Collection;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.gwtmobile.phonegap.client.Notification;
import com.gwtmobile.phonegap.client.Notification.Callback;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.DropDownItem;
import com.gwtmobile.ui.client.widgets.DropDownList;
import com.gwtmobile.ui.client.widgets.PasswordTextBox;
import com.gwtmobile.ui.client.widgets.TextBox;
import com.silverpeas.mobile.client.common.Database;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.pages.main.MainPage;
import com.silverpeas.mobile.client.persist.UserIds;
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
		initPage();						
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * Gestion du clique sur le bouton go.
	 * @param e
	 */
	@UiHandler("go")
	void connexion(ClickEvent e) {
		String login = loginField.getText();
		String password = passwordField.getText();
		login(login, password, domains.getSelectedValue(), false);
	}

	/**
	 * Transition de la demande de connexion au serveur.
	 * @param login
	 * @param password
	 * @param domainId
	 */
	private void login(String login, String password, String domainId, final boolean auto) {
		ServicesLocator.serviceConnection.login(login, password, domainId, new AsyncCallback<Void>() {
			public void onFailure(Throwable reason) {
				clearIds();
				//TODO : extract label
				//TODO : afficher le bon message d'erreur
				Notification.alert("Mot de passe incorrect", new Callback() {					
					public void onComplete() {						
						passwordField.setText("");
					}
				}, "Erreur", "OK");				
			}
			public void onSuccess(Void result) {
				if (!auto) storeIds();
				mainPage = new MainPage();
				goTo(mainPage);				
			}
		});
	}
	
	private void clearIds() {
		Database.open();
		final Entity<UserIds> userIdsEntity = GWT.create(UserIds.class);
		Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {			
			public void onSuccess() {
				userIdsEntity.all().destroyAll(new com.gwtmobile.persistence.client.Callback() {
					public void onSuccess() {						
						Persistence.flush();						
					}
				});
			}
		});
	}
	
	/**
	 * Mémorisation des identifiants de l'utilisateur.
	 */
	private void storeIds() {		
		Database.open();		
		final Entity<UserIds> userIdsEntity = GWT.create(UserIds.class);
		Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {			
			public void onSuccess() {
				userIdsEntity.all().destroyAll(new com.gwtmobile.persistence.client.Callback() {					
					public void onSuccess() {
						final UserIds userIds = userIdsEntity.newInstance();
						userIds.setLogin(loginField.getText());
						userIds.setPassword(passwordField.getText());
						userIds.setDomainId(domains.getSelectedValue());						
						Persistence.flush();						
					}
				});								
			}
		});		
	}
	
	private void initPage() {
		Database.open();
		final Entity<UserIds> userIdsEntity = GWT.create(UserIds.class);
		final Collection<UserIds> usersIds = userIdsEntity.all().limit(1);
		usersIds.count(new ScalarCallback<Integer>() {
			public void onSuccess(Integer result) {
				if(result == 1) {
					usersIds.each(new ScalarCallback<UserIds>() {
						public void onSuccess(UserIds result) {
							login(result.getLogin(), result.getPassword(), result.getDomainId(), true);
						}
					});
				} else {
					loadDomains();
				}				
			}
		});		
	}
	
	/**
	 * Récupération de la liste des domaines.
	 */
	private void loadDomains() {
		ServicesLocator.serviceConnection.getDomains(new AsyncCallback<List<DomainDTO>>() {
			public void onFailure(Throwable caught) {
				//TODO : extract label
				Notification.alert("Erreur système", new Callback() {					
					public void onComplete() {	}
				}, "Erreur", "OK");
			}

			public void onSuccess(List<DomainDTO> result) {
				if (result == null) {
					mainPage = new MainPage();
					goTo(mainPage);	
				} else {			
					Iterator<DomainDTO> iDomains = result.iterator();
					while (iDomains.hasNext()) {
						DomainDTO domain = iDomains.next();
						DropDownItem item = new DropDownItem();
						item.setValue(domain.getId());
						item.setText(domain.getName());
						domains.add(item);
					}
				}
			}
		});
	}
}
