package com.silverpeas.mobile.client.apps.gallery.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.common.app.View;

public class PictureViewerPage extends Page implements View {

	@UiField protected HTMLPanel content;
	@UiField protected Label title;
	
	private String photoId;
	private String galleryId;
	
	private static PictureViewerPageUiBinder uiBinder = GWT.create(PictureViewerPageUiBinder.class);

	interface PictureViewerPageUiBinder extends
			UiBinder<Widget, PictureViewerPage> {
	}

	public PictureViewerPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void init(String galleryId, String photoId) {
		this.galleryId = galleryId;
		this.photoId = photoId;
		//TODO : load picture
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
