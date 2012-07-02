package com.silverpeas.mobile.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;

public class FooterBar extends Composite {
	private ApplicationMessages msg =  GWT.create(ApplicationMessages.class);	
	private ApplicationResources res =  GWT.create(ApplicationResources.class);
	private InlineHTML label = new InlineHTML(msg.copyright());
	
	public FooterBar() {
		label.setHorizontalAlignment(Label.ALIGN_CENTER);
		label.setWordWrap(true);
		label.setStyleName(res.css().footer());
		
		
		initWidget(label);
		
	}
}
