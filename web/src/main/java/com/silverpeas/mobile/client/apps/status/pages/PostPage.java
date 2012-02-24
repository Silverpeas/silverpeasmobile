package com.silverpeas.mobile.client.apps.status.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.TextArea;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusLoadEvent;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusPostEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.AbstractStatusPostPagesEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPostedEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPostPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;

public class PostPage extends Page implements StatusPostPagesEventHandler, View{

	private static PostPageUiBinder uiBinder = GWT.create(PostPageUiBinder.class);
	
	@UiField TextArea textField;
	@UiField Button post;
	
	interface PostPageUiBinder extends UiBinder<Widget, PostPage> {
	}
	
	public PostPage(){
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractStatusPostPagesEvent.TYPE, this);
	}
	
	@UiHandler("post")
	void Post(ClickEvent e) {
		final String text = textField.getText();
		EventBus.getInstance().fireEvent(new StatusPostEvent(text));	
	}
	
	public void onStatusPost(StatusPostedEvent event) {
		stop();
		EventBus.getInstance().fireEvent(new StatusLoadEvent(1));	
		goBack(null);
	}
	
	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractStatusPostPagesEvent.TYPE, this);
	}
}
