package com.silverpeas.mobile.client.apps.gallery.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.widgets.ScrollPanel;
import com.silverpeas.mobile.client.apps.gallery.events.controller.LoadRemotePreviewPictureEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.remote.viewer.AbstractPictureViewerPageEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.remote.viewer.PictureViewLoadedEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.remote.viewer.PicturesViewerPageEventHandler;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryMessages;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryResources;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.PageView;
import com.silverpeas.mobile.client.common.app.View;

public class PictureViewerPage extends PageView implements View, PicturesViewerPageEventHandler {

	@UiField(provided = true) protected GalleryMessages msg = null;
	@UiField(provided = true) protected GalleryResources ressources = null;
	@UiField ScrollPanel container;
	@UiField protected Image content;
	@UiField protected Label title;
	
	private String photoId;
	private String galleryId;
	
	private static PictureViewerPageUiBinder uiBinder = GWT.create(PictureViewerPageUiBinder.class);

	interface PictureViewerPageUiBinder extends	UiBinder<Widget, PictureViewerPage> {
	}

	public PictureViewerPage() {
		ressources = GWT.create(GalleryResources.class);		
		ressources.css().ensureInjected();
		msg = GWT.create(GalleryMessages.class);
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
		content.setUrl(event.getPhoto().getDataPhoto());
		title.setText(event.getPhoto().getTitle());
		container.addStyleName(ressources.css().localPicture());
	}
}
