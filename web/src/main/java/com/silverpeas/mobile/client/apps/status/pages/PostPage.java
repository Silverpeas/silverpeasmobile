package com.silverpeas.mobile.client.apps.status.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.widgets.TextArea;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.base.PageContent;

public class PostPage extends PageContent implements View {

	private static PostPageUiBinder uiBinder = GWT.create(PostPageUiBinder.class);
	
	@UiField TextArea textField;
		
	interface PostPageUiBinder extends UiBinder<Widget, PostPage> {
	}
	
	public PostPage(){
		initWidget(uiBinder.createAndBindUi(this));		
		textField.setHeight(Window.getClientHeight()/2+"px");
		
		/*header.setRightButtonClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				clickGesture(new Command() {					
					@Override
					public void execute() {
						final String text = textField.getText();
						EventBus.getInstance().fireEvent(new StatusPostEvent(text));
						goBack(null);
						
					}
				});						
			}
		});*/
	}

	@Override
	public void stop() {		
	}
}
