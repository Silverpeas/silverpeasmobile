package com.silverpeas.mobile.client.pages.agenda;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.common.ServicesLocator;

public class AgendaPage extends Page {

	private static AgendaPageUiBinder uiBinder = GWT.create(AgendaPageUiBinder.class);

	interface AgendaPageUiBinder extends UiBinder<Widget, AgendaPage> {
	}

	public AgendaPage() {
		initWidget(uiBinder.createAndBindUi(this));
		ServicesLocator.serviceAgenda.viewAgenda(new AsyncCallback<Void>() {
			public void onFailure(Throwable reason) {
				Window.alert("Loading error");
			}
			public void onSuccess(Void result) {
								
			}
		});	
	}
}
