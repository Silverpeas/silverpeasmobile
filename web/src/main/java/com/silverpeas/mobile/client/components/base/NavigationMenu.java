package com.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class NavigationMenu extends Composite {

	private static NavigationMenuUiBinder uiBinder = GWT.create(NavigationMenuUiBinder.class);

	interface NavigationMenuUiBinder extends UiBinder<Widget, NavigationMenu> {
	}

	public NavigationMenu() {
		initWidget(uiBinder.createAndBindUi(this));
		setVisible(false);
	}

}
