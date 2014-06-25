package com.silverpeas.mobile.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.silverpeas.mobile.client.common.ErrorManager;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.event.ExceptionEvent;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;
import com.silverpeas.mobile.client.components.base.Page;
import com.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import com.silverpeas.mobile.client.pages.main.AppList;
import com.silverpeas.mobile.client.persist.User;
import com.silverpeas.mobile.client.rebind.ConfigurationProvider;

public class SpMobil implements EntryPoint{
		
	public final static ConfigurationProvider configuration = GWT.create(ConfigurationProvider.class);
	public final static Page mainPage = new Page();
	
	/**
	 * Init. spmobile.
	 */
	public void onModuleLoad() {
				
		EventBus.getInstance().addHandler(ExceptionEvent.TYPE, new ErrorManager());
		loadIds();
		
		if (MobilUtils.isRetina()) {
			RootLayoutPanel.get().addStyleName("webappIosRetina");			
		} else if (MobilUtils.isIOS()) {			
			RootLayoutPanel.get().addStyleName("webappIos");			
		}		
	}
	
	/**
	 * Auto login.
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
				RootPanel.get().clear();
				RootPanel.get().add(connexionPage);
			}
			public void onSuccess(Void result) {
				mainPage.setContent(new AppList());
				RootPanel.get().clear();
				RootPanel.get().add(mainPage);
			}
		});
	}
	
	/**
	 * Clean ids in SQL Web Storage.
	 */
	public void clearIds() {
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {					
			storage.clear();
		}		
	}
	
	/**
	 * Load ids in SQL Web Storage.
	 */
	private void loadIds() {		
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {						
			String dataItem = storage.getItem("userConnected");			
			if (dataItem != null) {
				User user = User.getInstance(dataItem);	
				String password = decryptPassword(user.getPassword());
				if (password != null) {
					login(user.getLogin(), password, user.getDomainId(), true);
				}
			} else {
				ConnexionPage connexionPage = new ConnexionPage();
				RootPanel.get().clear();
				RootPanel.get().add(connexionPage);
			}
		}
	}
	
	/**
	 * Decrypt password in SQL Web Storage.
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
