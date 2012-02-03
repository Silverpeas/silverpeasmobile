package com.silverpeas.mobile.client.apps.gallery.pages;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.persistence.client.Collection;
import com.gwtmobile.persistence.client.CollectionCallback;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.gwtmobile.phonegap.client.Camera;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.Transition;
import com.gwtmobile.ui.client.widgets.DropDownItem;
import com.gwtmobile.ui.client.widgets.DropDownList;
import com.gwtmobile.ui.client.widgets.HeaderPanel;
import com.gwtmobile.ui.client.widgets.HorizontalPanel;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.silverpeas.mobile.client.apps.gallery.events.app.internal.GalleryStopEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.GalleryLoadSettingsEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.GallerySaveSettingsEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.AbstractGalleryPagesEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryLoadedSettingsEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryNewInstanceLoadedEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryPagesEventHandler;
import com.silverpeas.mobile.client.apps.gallery.persistances.Picture;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryMessages;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryResources;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.common.Database;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.components.icon.Icon;
import com.silverpeas.mobile.shared.dto.AlbumDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

/**
 * Pictures gallery mobile application.
 * @author svuillet
 */
public class GalleryPage extends Page implements GalleryPagesEventHandler, View {

	private static GalleryPageUiBinder uiBinder = GWT.create(GalleryPageUiBinder.class);
	@UiField(provided = true) protected GalleryMessages msg = null;
	@UiField(provided = true) protected GalleryResources ressources = null;
	@UiField protected Icon takePicture, local, sync, remote;
	@UiField protected HeaderPanel footer, header;
	@UiField protected HorizontalPanel content;
	@UiField protected DropDownList albums;
	@UiField protected Label footerTitle, gallery;
	@UiField protected HTMLPanel htmlPanel;
	@UiField protected ListPanel place;
	
	private static int nbPictures;
	private static int ratioPicture;
	private static boolean uploading, stopScheduler;
	private ApplicationInstanceDTO currentInstance;
	
	interface GalleryPageUiBinder extends UiBinder<Widget, GalleryPage> {
	}

	public GalleryPage() {
		ressources = GWT.create(GalleryResources.class);		
		ressources.css().ensureInjected();
		msg = GWT.create(GalleryMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractGalleryPagesEvent.TYPE, this);
		
		// load previous gallery and album selection
		EventBus.getInstance().fireEvent(new GalleryLoadSettingsEvent());
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
		Camera.Options options = new Camera.Options();
		options.quality(50);
		//options.destinationType(DestinationType.FILE_URI); // for optimal performances
		
		Camera.getPicture(new Camera.Callback() {			
			public void onSuccess(final String imageData) {				
				Notification.activityStart();
				Database.open();		
				final Entity<Picture> pictureEntity = GWT.create(Picture.class);				
				Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {			
					public void onSuccess() {
						final Picture pic = pictureEntity.newInstance();
						pic.setData(imageData);
						Persistence.flush();
						Notification.activityStop();
					}
				});
			}

			public void onError(String message) {
				// TODO : manage cancel photo taking
				Notification.activityStop();
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(message)));	
			}
		}, options);
	}
	
	/**
	 * Browser local pictures.
	 * @param e
	 */
	@UiHandler("local")
	void localPictures(ClickEvent e){
		Database.open();
		final Entity<Picture> pictureEntity = GWT.create(Picture.class);
		final Collection<Picture> pictures = pictureEntity.all();
		pictures.count(new ScalarCallback<Integer>() {					
			@Override
			public void onSuccess(final Integer count) {
				if (count == 0) {
					Notification.alert("No locals pictures", null, "Information", "OK");
				} else {
					Notification.activityStart();
					pictures.list((new CollectionCallback<Picture>(){

						@Override
						public void onSuccess(Picture[] pictures) {
							final PicturePage picturePage = new PicturePage();
							picturePage.setPictures(pictures);
							Notification.activityStop();
							goTo(picturePage, Transition.SLIDE);
						}
						
					}));				
				}
			}
		});		
	}
	
	/**
	 * Send local pictures to server
	 * @param e
	 */
	@UiHandler("sync")
	void syncPictures(ClickEvent e){
		Database.open();
		final Entity<Picture> pictureEntity = GWT.create(Picture.class);
		final Collection<Picture> pictures = pictureEntity.all();
		pictures.count(new ScalarCallback<Integer>() {
			
			@Override
			public void onSuccess(final Integer count) {
				
				if (count > 0) {
					Notification.progressStart("Uploading", count + " pictures");
					nbPictures = 0;	
					ratioPicture = 100 / count;
					pictures.list(new CollectionCallback<Picture>() {
						@Override
						public void onSuccess(Picture[] results) {
							uploadPicture(count, results, pictures);					
						}					
					});
				} else {
					Notification.alert("Nothing to upload", null, "Information", "OK");
				}					
			}
		});	
	}
	
	/**
	 * Upload one picture to gallery.
	 * @param count
	 */
	private void uploadPicture(final Integer count, final Picture[] results, final Collection<Picture> pictures) {
		
		/*
		// For optimal performances
		FileReader reader = File.newReaderInstance();
		reader.readAsDataURL("");
		reader.onLoadEnd(callback);
		*/		
		
		uploading = false;
		stopScheduler = false;
		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {								
			@Override
			public boolean execute() {					
				
				if (uploading == false) {					
					for (int i = 0; i < results.length; i++) {
						final Picture picture = results[i];				
					
						String name = picture.getName();
						if (picture.getName() == null || picture.getName().isEmpty()) {
							name = picture.getId();
						}
						
						uploading = true;
						ServicesLocator.serviceGallery.uploadPicture(name, picture.getData(), new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
								stopScheduler = true;
							}

							@Override
							public void onSuccess(Void result) {
								// remove picture from html5 bdd
								pictures.remove(picture);
								
								// compute job progress
								nbPictures++;
								Notification.progressValue(nbPictures*ratioPicture);								
								if (count > nbPictures) {
									uploading = false;
								} else {
									Notification.progressStop();
									stopScheduler = true;
								}								
							}
						});						
					}										
				}				
				return stopScheduler;
			}
		}, 300);
	}
	
	@UiHandler("place")
	void browseAllAvailableGallerie(SelectionChangedEvent event) {
		if (event.getSelection() == 0) {
			NavigationApp app = new NavigationApp();
			app.setTypeApp("gallery");
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
		goTo(remoteBrowser, Transition.SLIDE);
	}

	@Override
	public void onLoadedSettings(GalleryLoadedSettingsEvent event) {
		ApplicationInstanceDTO app = new ApplicationInstanceDTO();
		app.setId(event.getSettings().getSelectedGalleryId());
		app.setLabel(event.getSettings().getSelectedGalleryLabel());
		app.setType("gallery");		
		displayPlace(event.getAlbums(), app, event.getSettings().getSelectedAlbumId());
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractGalleryPagesEvent.TYPE, this);		
	}

	@Override
	public void onNewGalleryInstanceLoaded(GalleryNewInstanceLoadedEvent event) {
		displayPlace(event.getAlbums(), event.getInstance(), "");
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
	}
	
	@Override
	public void goBack(Object returnValue) {
		stop();
		EventBus.getInstance().fireEvent(new GalleryStopEvent());		
		super.goBack(returnValue);
	}
}
