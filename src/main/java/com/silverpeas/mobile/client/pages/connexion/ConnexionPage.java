package com.silverpeas.mobile.client.pages.connexion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.silverpeas.mobile.client.pages.main.MainPage;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;

public class ConnexionPage extends Page {

	private static ConnexionPageUiBinder uiBinder = GWT.create(ConnexionPageUiBinder.class);
	private MainPage mainPage;
	@UiField(provided = true) protected ApplicationMessages msg = null;
	@UiField(provided = true) protected ApplicationResources res = null;
	@UiField Button go;

	interface ConnexionPageUiBinder extends UiBinder<Widget, ConnexionPage> {
	}

	public ConnexionPage() {
		res = GWT.create(ApplicationResources.class);		
		res.css().ensureInjected();
		msg = GWT.create(ApplicationMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("go")
	void connexion(ClickEvent e) {
		GWT.runAsync(ConnexionPage.class, new RunAsyncCallback() {
			
			public void onSuccess() {
				mainPage = new MainPage();
				goTo(mainPage);				
			}
			
			public void onFailure(Throwable reason) {
				Window.alert("Loading error");
			}
		});
	}
}
