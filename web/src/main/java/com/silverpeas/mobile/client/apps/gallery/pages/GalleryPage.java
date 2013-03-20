package com.silverpeas.mobile.client.apps.gallery.pages;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.page.Transition;
import com.gwtmobile.ui.client.widgets.DropDownItem;
import com.gwtmobile.ui.client.widgets.DropDownList;
import com.gwtmobile.ui.client.widgets.HeaderPanel;
import com.gwtmobile.ui.client.widgets.HorizontalPanel;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.silverpeas.mobile.client.apps.gallery.events.app.internal.GalleryStopEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.GalleryLoadSettingsEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.GallerySaveSettingsEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.LoadLocalPicturesEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.SyncPicturesEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.TakePictureEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.AbstractGalleryPagesEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryEndUploadEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryLoadedSettingsEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryLocalPicturesLoadedEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryNewInstanceLoadedEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryPagesEventHandler;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryPictureUploadedEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryStartingUploadEvent;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryMessages;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryResources;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.PageView;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;
import com.silverpeas.mobile.client.components.icon.Icon;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.gallery.AlbumDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

/**
 * Pictures gallery mobile application.
 * @author svuillet
 */
public class GalleryPage extends PageView implements GalleryPagesEventHandler, View {

	private static GalleryPageUiBinder uiBinder = GWT.create(GalleryPageUiBinder.class);
	@UiField(provided = true) protected GalleryMessages msg = null;
	@UiField(provided = true) protected ApplicationMessages globalMsg = null;
	@UiField(provided = true) protected GalleryResources ressources = null;
	@UiField protected Icon takePicture, local, sync, remote;
	@UiField protected HeaderPanel header;
	@UiField protected HorizontalPanel content;
	@UiField protected DropDownList albums;
	@UiField protected Label gallery;
	@UiField protected HTMLPanel htmlPanel;
	@UiField protected ListPanel place;
	
	// Data for upload
	private static int nbPictures;
	private static int ratioPicture;
	
	private ApplicationInstanceDTO currentInstance;
	
	interface GalleryPageUiBinder extends UiBinder<Widget, GalleryPage> {
	}

	public GalleryPage() {
		ressources = GWT.create(GalleryResources.class);		
		ressources.css().ensureInjected();
		msg = GWT.create(GalleryMessages.class);
		globalMsg = GWT.create(ApplicationMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractGalleryPagesEvent.TYPE, this);
		// load previous gallery and album selection
		EventBus.getInstance().fireEvent(new GalleryLoadSettingsEvent());
				
		if (MobilUtils.isPhoneGap() == false) {
			takePicture.setInactive(true);
			local.setInactive(true);
			sync.setInactive(true);			
		}
		remote.setInactive(true);
	}
	
	/**
	 * Store in HTML5 database album selected.
	 * @param e
	 */
	@UiHandler("albums")
	void onAlbumChange(ValueChangeEvent<String> e) {
		if (!e.getValue().isEmpty()) {			
			// Send message to controller for save settings.
			EventBus.getInstance().fireEvent(new GallerySaveSettingsEvent(currentInstance, albums.getSelectedValue()));	
		}
	}	
	
	/**
	 * Take a picture and store it in local database.
	 * @param e
	 */
	@UiHandler("takePicture")
	void takePicture(ClickEvent e) {
		EventBus.getInstance().fireEvent(new TakePictureEvent());
	}
	
	/**
	 * Load local pictures.
	 * @param e
	 */
	@UiHandler("local")
	void localPictures(ClickEvent e){
		EventBus.getInstance().fireEvent(new LoadLocalPicturesEvent());
	}
	
	/**
	 * Browser local pictures.
	 * @param e
	 */
	@Override
	public void onLocalPicturesLoaded(GalleryLocalPicturesLoadedEvent event) {
		if (event.getPictures() == null) {
			Notification.alert(msg.localPicture_empty(), null, globalMsg.infoTitle(), globalMsg.ok());
		} else {
			final LocalPictureViewerPage picturePage = new LocalPictureViewerPage();
			picturePage.setPictures(event.getPictures());
			Notification.activityStop();
			goTo(picturePage, Transition.SLIDE);
		}
	}
	
	/**
	 * Send local pictures to server
	 * @param e
	 */
	@UiHandler("sync")
	void syncPictures(ClickEvent e) {
		EventBus.getInstance().fireEvent(new SyncPicturesEvent(currentInstance.getId(), albums.getSelectedValue()));
	}
	
	@Override
	public void onStartingUpload(GalleryStartingUploadEvent event) {
		if (event.getPicturesNumber() > 0) {
			Notification.progressStart(msg.localPicture_uploading(), event.getPicturesNumber() + " " + msg.localPicture());
			nbPictures = 0;	
			ratioPicture = 100 / event.getPicturesNumber();
		} else {
			Notification.alert(msg.localPicture_empty(), null, globalMsg.infoTitle(), globalMsg.ok());
		}		
	}
	
	@Override
	public void onEndUpload(GalleryEndUploadEvent event) {
		Notification.progressStop();		
	}
	
	@Override
	public void onPictureUploaded(GalleryPictureUploadedEvent event) {
		// compute job progress
		nbPictures++;
		Notification.progressValue(nbPictures*ratioPicture);
	}
	
	@UiHandler("place")
	void browseAllAvailableGallerie(SelectionChangedEvent event) {
		if (event.getSelection() == 0) {
			NavigationApp app = new NavigationApp();
			app.setTypeApp("gallery");
			app.setTitle("Gallery app browser"); //TODO : i18n
			app.start(this);
		}
	}
	
	/**
	 * Browse remote galleries.
	 * @param e
	 */
	@UiHandler("remote")
	void remotePictures(ClickEvent e) {
		final GalleryRemoteBrowser remoteBrowser = new GalleryRemoteBrowser();
		remoteBrowser.setGalleryId(currentInstance.getId());
		remoteBrowser.setAlbumId(albums.getSelectedValue());
		remoteBrowser.init();
		goTo(remoteBrowser, Transition.SLIDE);
	}

	@Override
	public void onLoadedSettings(GalleryLoadedSettingsEvent event) {
		ApplicationInstanceDTO app = new ApplicationInstanceDTO();
		app.setId(event.getSettings().getSelectedGalleryId());
		app.setLabel(event.getSettings().getSelectedGalleryLabel());
		app.setType(Apps.gallery.name());		
		displayPlace(event.getAlbums(), app, event.getSettings().getSelectedAlbumId());
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractGalleryPagesEvent.TYPE, this);		
	}

	@Override
	public void onNewGalleryInstanceLoaded(GalleryNewInstanceLoadedEvent event) {
		displayPlace(event.getAlbums(), event.getInstance(), "");
		Notification.activityStop();
	}
	
	/**
	 * Display informations on working gallery and album.
	 * @param albumsDTO
	 * @param appDTO
	 * @param selectedAlbumId
	 */
	private void displayPlace(List<AlbumDTO> albumsDTO, ApplicationInstanceDTO appDTO, String selectedAlbumId) {
		// gallery
		gallery.setText(appDTO.getLabel());
		// albums
		albums.getListBox().clear();
		DropDownItem emptyA = new DropDownItem();
		albums.add(emptyA);
		int i = 0;
		int indexSelected = 0;
		for (AlbumDTO album :albumsDTO) {
			DropDownItem a = new DropDownItem();
			a.setText(album.getName());
			a.setValue(album.getId());
			albums.add(a);
			if (album.getId().equals(selectedAlbumId)) {
				indexSelected = i + 1;
			}
			i++;
		}
		albums.getListBox().setSelectedIndex(indexSelected);
		// store instance gallery
		this.currentInstance = appDTO;
		remote.setInactive(false);
	}
	
	@Override
	public void goBack(Object returnValue) {
		stop();
		EventBus.getInstance().fireEvent(new GalleryStopEvent());		
		super.goBack(returnValue);
	}
}