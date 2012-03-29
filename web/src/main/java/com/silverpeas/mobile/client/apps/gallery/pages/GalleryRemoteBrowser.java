package com.silverpeas.mobile.client.apps.gallery.pages;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.HorizontalPanel;
import com.gwtmobile.ui.client.widgets.ListItem;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.silverpeas.mobile.client.apps.gallery.events.controller.RemotePicturesLoadEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.remote.AbstractRemotePicturesPageEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.remote.RemotePictureLoadedEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.remote.RemotePicturesPageEventHandler;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryResources;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.gallery.PhotoDTO;

public class GalleryRemoteBrowser extends Page implements View, RemotePicturesPageEventHandler {

	private static GalleryRemoteBrowserUiBinder uiBinder = GWT.create(GalleryRemoteBrowserUiBinder.class);
	private String galleryId;
	private String albumId;
	private List<PhotoDTO> photos = null;
	
	@UiField(provided = true) protected GalleryResources ressources = null;
	
	@UiField ListPanel content;

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
		ressources = GWT.create(GalleryResources.class);		
		ressources.css().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void init() {
		EventBus.getInstance().addHandler(AbstractRemotePicturesPageEvent.TYPE, this);
		EventBus.getInstance().fireEvent(new RemotePicturesLoadEvent(galleryId, albumId));
	}

	@Override
	public void onPicturesLoaded(RemotePictureLoadedEvent event) {
		this.photos = event.getPhotos();
		if (photos.isEmpty()) {
			content.add(new Label("no pictures"));
		} else {			
			int photoMaxHeight = 0;
			for (PhotoDTO photo : photos) {
				ListItem item = new ListItem();
				HorizontalPanel panel = new HorizontalPanel();				
				Label title = new Label(photo.getTitle());
				title.setStylePrimaryName(ressources.css().remotePictureTitle());
				panel.add(title);				
				Image photoW = new Image(photo.getDataPhotoTiny());
				photoW.getElement().setId(photo.getId());
				panel.add(photoW);			
				item.add(panel);
				content.add(item);				
				if (photoW.getHeight() > photoMaxHeight) {
					photoMaxHeight = photoW.getHeight();
				}
			}
			Iterator<Widget> it = content.iterator();
			while (it.hasNext()) {
				ListItem item = (ListItem) it.next();
				item.setHeight(photoMaxHeight + "px");
			}			
		}	
	}
	
	@Override
	public void goBack(Object returnValue) {
		stop();				
		super.goBack(returnValue);
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractRemotePicturesPageEvent.TYPE, this);		
	}
}