package com.silverpeas.mobile.client.apps.gallery.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;

public class GalleryRemoteBrowser extends Page {

	private static GalleryRemoteBrowserUiBinder uiBinder = GWT.create(GalleryRemoteBrowserUiBinder.class);
	


	interface GalleryRemoteBrowserUiBinder extends
			UiBinder<Widget, GalleryRemoteBrowser> {
	}

	public GalleryRemoteBrowser() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
