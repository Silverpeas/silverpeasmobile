package com.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.common.navigation.PageHistory;

public class PageHeader extends Composite {

	private static PageHeaderUiBinder uiBinder = GWT.create(PageHeaderUiBinder.class);

	interface PageHeaderUiBinder extends UiBinder<Widget, PageHeader> {
	}
	
	@UiField protected Anchor menu, back;

	public PageHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	@UiHandler("menu")
	void onMenu(ClickEvent event) {
		Window.alert("menu");
		Window.confirm("Yes or No ?");
	}
	
	@UiHandler("back")
	void onBack(ClickEvent event) {
		PageHistory.getInstance().back();
	}	
	
	public void setVisibleBackButton(boolean visible) {
		back.setVisible(visible);
	}

}
