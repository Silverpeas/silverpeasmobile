package com.silverpeas.mobile.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.Label;

public class FooterBar extends Composite{
	private HeaderPanel headerPanel = new HeaderPanel();
	private Label label = new Label("Copyright Silverpeas 1999-2011");
	
	public FooterBar(){
		label.setHorizontalAlignment(Label.ALIGN_CENTER);
		label.setWordWrap(true);
		headerPanel.add(label);
	}
}
