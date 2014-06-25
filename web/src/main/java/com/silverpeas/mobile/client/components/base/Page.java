package com.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class Page extends Composite {

	private static PageUiBinder uiBinder = GWT.create(PageUiBinder.class);

	interface PageUiBinder extends UiBinder<Widget, Page> {
	}
	
	@UiField protected SimplePanel contentPlace;
	protected PageContent content;

	public Page() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setContent(PageContent content) {
		this.content = content;
		contentPlace.setWidget(content);
	}

}
