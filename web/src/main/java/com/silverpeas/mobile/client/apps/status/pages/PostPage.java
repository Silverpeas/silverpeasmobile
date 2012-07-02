package com.silverpeas.mobile.client.apps.status.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.HeaderPanel;
import com.gwtmobile.ui.client.widgets.TextArea;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusPostEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;

public class PostPage extends Page implements View {

	private static PostPageUiBinder uiBinder = GWT.create(PostPageUiBinder.class);
	
	@UiField TextArea textField;
	@UiField HeaderPanel header;
	
	interface PostPageUiBinder extends UiBinder<Widget, PostPage> {
	}
	
	public PostPage(){
		initWidget(uiBinder.createAndBindUi(this));		
		textField.setHeight(Window.getClientHeight()/2+"px");
		header.setRightButtonClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				final String text = textField.getText();
				EventBus.getInstance().fireEvent(new StatusPostEvent(text));
				goBack(null);
			}
		});
	}

	@Override
	public void stop() {		
	}
}
