package com.silverpeas.mobile.client.pages.status;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;

public class StatusPage extends Page {

	@UiField protected Button post;
	
	private static StatusPageUiBinder uiBinder = GWT.create(StatusPageUiBinder.class);

	interface StatusPageUiBinder extends UiBinder<Widget, StatusPage> {
	}

	public StatusPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("post")
	void post(ClickEvent e) {
		goBack(null);
	}
}
