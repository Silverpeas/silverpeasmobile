package com.silverpeas.mobile.client.apps.gallery.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.ScrollPanel;
import com.silverpeas.mobile.client.apps.gallery.events.controller.LoadRemotePreviewPictureEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.remote.viewer.AbstractPictureViewerPageEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.remote.viewer.PictureViewLoadedEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.remote.viewer.PicturesViewerPageEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;

public class PictureViewerPage extends Page implements View, PicturesViewerPageEventHandler {

	@UiField protected ScrollPanel content;
	@UiField protected Label title;
	
	private String photoId;
	private String galleryId;
	
	private static PictureViewerPageUiBinder uiBinder = GWT.create(PictureViewerPageUiBinder.class);

	interface PictureViewerPageUiBinder extends	UiBinder<Widget, PictureViewerPage> {
	}

	public PictureViewerPage() {
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractPictureViewerPageEvent.TYPE, this);
	}
	
	public void init(String galleryId, String photoId) {
		this.galleryId = galleryId;
		this.photoId = photoId;
		EventBus.getInstance().fireEvent(new LoadRemotePreviewPictureEvent(galleryId, photoId));
	}
	
	@Override
	public void goBack(Object returnValue) {
		stop();				
		super.goBack(returnValue);
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractPictureViewerPageEvent.TYPE, this);		
	}

	@Override
	public void onPictureLoaded(PictureViewLoadedEvent event) {
		Image image = new Image(event.getPhoto().getDataPhoto());
		content.add(image);
		title.setText(event.getPhoto().getTitle());
		image.getElement().setAttribute("style", "min-width:100%;max-width:100%;");
	}
}
