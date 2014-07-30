package com.silverpeas.mobile.client.apps.gallery.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.components.base.PageContent;

public class MediaNavigationPage extends PageContent {

	private static MediaNavigationPageUiBinder uiBinder = GWT.create(MediaNavigationPageUiBinder.class);
	private String instanceId;

	interface MediaNavigationPageUiBinder extends UiBinder<Widget, MediaNavigationPage> {
	}

	public MediaNavigationPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setAlbumId(String rootAlbumId) {
		
	}
	
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

}
