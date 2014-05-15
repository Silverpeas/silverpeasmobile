package com.silverpeas.mobile.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.common.ErrorManager;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.event.ExceptionEvent;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;
import com.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import com.silverpeas.mobile.client.pages.main.MainPage;
import com.silverpeas.mobile.client.persist.User;
import com.silverpeas.mobile.client.rebind.ConfigurationProvider;


//TODO : Call REST services
//TODO : Gin injection client side
//TODO : internationalisation globale
//TODO : generic ConfigurationProvider

public class SpMobil implements EntryPoint{
	
	private static boolean noUserIdsStore = true;
	public final static ConfigurationProvider configuration = GWT.create(ConfigurationProvider.class);
	
	/**
	 * Point de lancement.
	 */
	public void onModuleLoad() {
				
		EventBus.getInstance().addHandler(ExceptionEvent.TYPE, new ErrorManager());		
		
		loadIds();
						
		if (MobilUtils.isRetina() && MobilUtils.isPhoneGap() == false) {
			RootLayoutPanel.get().addStyleName("webappIosRetina");			
		} else if (MobilUtils.isIOS() && MobilUtils.isPhoneGap() == false) {			
			RootLayoutPanel.get().addStyleName("webappIos");			
		}
		
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
	public void clearIds() {
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {					
			storage.clear();
		}		
	}
	
	/**
	 * Chargement des ids mémorisés en SQL Web Storage.
	 */
	private void loadIds() {		
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {						
			String dataItem = storage.getItem("userConnected");			
			if (dataItem != null) {
				User user = User.getInstance(dataItem);
				noUserIdsStore = false;	
				String password = decryptPassword(user.getPassword());
				if (password != null) {
					login(user.getLogin(), password, user.getDomainId(), true);
				}
			}			
			
			
		}
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
			EventBus.getInstance().fireEvent(new ErrorEvent(e));
		}
		return plainPassword;
	}
	
}
