package com.silverpeas.mobile.client.apps.gallery.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.gallery.events.controller.RemotePicturesLoadEvent;
import com.silverpeas.mobile.client.common.EventBus;

public class GalleryRemoteBrowser extends Page {

	private static GalleryRemoteBrowserUiBinder uiBinder = GWT.create(GalleryRemoteBrowserUiBinder.class);
	private String galleryId;
	private String albumId;

	public void setGalleryId(String galleryId) {
		this.galleryId = galleryId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	interface GalleryRemoteBrowserUiBinder extends
			UiBinder<Widget, GalleryRemoteBrowser> {
	}

	public GalleryRemoteBrowser() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void init() {
		EventBus.getInstance().fireEvent(new RemotePicturesLoadEvent(galleryId, albumId));
	}
}