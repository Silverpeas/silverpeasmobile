package com.silverpeas.mobile.client.pages.status;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.TextBox;

public class StatusPage extends Page {
	
	private static StatusPageUiBinder uiBinder = GWT.create(StatusPageUiBinder.class);
	
	@UiField TextBox textField;
	@UiField Button post;
	
	interface StatusPageUiBinder extends UiBinder<Widget, StatusPage> {
	}

	public StatusPage() {
		initWidget(uiBinder.createAndBindUi(this));	
	}
	
	/*@UiHandler("post")
	void Post(ClickEvent e) {
		final String text = textField.getText();
		ServicesLocator.serviceRSE.updateStatus(text, new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Window.alert("Update status failed.");
            }
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				Window.alert("Update status done.");
				goBack(null);
			}
		});
	}*/
}