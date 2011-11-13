package com.silverpeas.mobile.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.gwtmobile.persistence.client.Collection;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.gwtmobile.phonegap.client.Notification;
import com.gwtmobile.phonegap.client.Notification.Callback;
import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.common.Database;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import com.silverpeas.mobile.client.pages.main.MainPage;
import com.silverpeas.mobile.client.persist.UserIds;
import com.silverpeas.mobile.client.rebind.ConfigurationProvider;

public class SpMobil implements EntryPoint{
	
	private static boolean noUserIdsStore = true;
	public final static ConfigurationProvider configuration = GWT.create(ConfigurationProvider.class);
	
	/**
	 * Point de lancement.
	 */
	public void onModuleLoad() {		
		loadIds();
		
		Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand() {			
			public boolean execute() {
				if (noUserIdsStore) {
					ConnexionPage connexionPage = new ConnexionPage();
					Page.load(connexionPage);
					return false;
				}
				return true;
			}
		}, 300);
	}
	
	/**
	 * Login automatique.
	 * @param login
	 * @param password
	 * @param domainId
	 * @param auto
	 */
	private void login(String login, String password, String domainId, final boolean auto) {
		ServicesLocator.serviceConnection.login(login, password, domainId, new AsyncCallback<Void>() {
			public void onFailure(Throwable reason) {
				clearIds();
				ConnexionPage connexionPage = new ConnexionPage();
				Page.load(connexionPage);		
			}
			public void onSuccess(Void result) {
				MainPage mainPage = new MainPage();
				Page.load(mainPage);				
			}
		});
	}
	
	/**
	 * Suppression des ids mémorisés en SQL Web Storage.
	 */
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
	 * Chargement des ids mémorisés en SQL Web Storage.
	 */
	private void loadIds() {
		Database.open();
		final Entity<UserIds> userIdsEntity = GWT.create(UserIds.class);
		final Collection<UserIds> usersIds = userIdsEntity.all().limit(1);		
		usersIds.one(new ScalarCallback<UserIds>() {
			public void onSuccess(UserIds result) {
				noUserIdsStore = false;	
				String password = decryptPassword(result.getPassword());
				if (password != null) {
					login(result.getLogin(), password, result.getDomainId(), true);
				}
			}
		});
	}
	
	/**
	 * Decrypte le mot de passe mémorisés en SQL Web Storage.
	 * @param passwordEncrysted
	 * @return
	 */
	private String decryptPassword(String passwordEncrysted) {
		TripleDesCipher cipher = new TripleDesCipher();
		cipher.setKey(configuration.getDESKey().getBytes());
		String plainPassword = null;
		try {
			plainPassword = cipher.decrypt(passwordEncrysted);
		} catch (Exception e) {
			Notification.alert("Erreur système", new Callback() {					
				public void onComplete() {
				}
			}, "Erreur", "OK");
		}
		return plainPassword;
	}
	
}
