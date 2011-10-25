package com.silverpeas.mobile.client.pages.agenda;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;

public class AgendaPage extends Page {

	private static AgendaPageUiBinder uiBinder = GWT.create(AgendaPageUiBinder.class);

	interface AgendaPageUiBinder extends UiBinder<Widget, AgendaPage> {
	}

	public AgendaPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
