package com.silverpeas.mobile.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.utils.Utils;
import com.silverpeas.mobile.client.pages.connexion.ConnexionPage;

public class SpMobil implements EntryPoint{

	ConnexionPage connexionPage;
	
	public void onModuleLoad() {
		new Timer() {
			@Override
			public void run() {
				if (connexionPage == null) {
					Utils.Console("Loading main ui...");
					connexionPage = new ConnexionPage();
					Page.load(connexionPage);
				}
				else {
					this.cancel();
				}
			}
		}.scheduleRepeating(50);		
	}
}
