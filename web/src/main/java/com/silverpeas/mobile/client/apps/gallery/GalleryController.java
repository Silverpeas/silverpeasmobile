package com.silverpeas.mobile.client.apps.gallery;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmobile.persistence.client.Collection;
import com.gwtmobile.persistence.client.CollectionCallback;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.gwtmobile.phonegap.client.Camera;
import com.silverpeas.mobile.client.apps.gallery.events.controller.AbstractGalleryControllerEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.DeleteLocalPictureEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.GalleryControllerEventHandler;
import com.silverpeas.mobile.client.apps.gallery.events.controller.GalleryLoadSettingsEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.GallerySaveSettingsEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.LoadLocalPicturesEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.SyncPicturesEvent;
import com.silverpeas.mobile.client.apps.gallery.events.controller.TakePictureEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryEndUploadEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryLoadedSettingsEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryLocalPicturesLoadedEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryNewInstanceLoadedEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryPictureUploadedEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.GalleryStartingUploadEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.local.DeletedLocalPictureEvent;
import com.silverpeas.mobile.client.apps.gallery.persistances.GallerySettings;
import com.silverpeas.mobile.client.apps.gallery.persistances.Picture;
import com.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationEventHandler;
import com.silverpeas.mobile.client.common.Database;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.AlbumDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class GalleryController implements Controller, GalleryControllerEventHandler, NavigationEventHandler {
	
	// Temporary data for synch
	private transient static Collection<Picture> localsPicturesList;
	private transient static Picture[] localsPictures;
	private transient static int indexNextPictureToUpload;
	
	public GalleryController() {
		super();
		EventBus.getInstance().addHandler(AbstractGalleryControllerEvent.TYPE, this);
		EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
	}

	/**
	 * Load stored settings and send settings on bus.
	 */
	@Override
	public void loadSettings(GalleryLoadSettingsEvent event) {
		Database.open();		
		final Entity<GallerySettings> settingsEntity = GWT.create(GallerySettings.class);
		final Collection<GallerySettings> settings = settingsEntity.all().limit(1);			
		settings.one(new ScalarCallback<GallerySettings>() {
			public void onSuccess(final GallerySettings settings) {				
				ServicesLocator.serviceGallery.getAllAlbums(settings.getSelectedGalleryId(), new AsyncCallback<List<AlbumDTO>>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
					}
					@Override
					public void onSuccess(List<AlbumDTO> albums) {
						EventBus.getInstance().fireEvent(new GalleryLoadedSettingsEvent(settings, albums));
					}			
				});
				//TODO : gestion suppression instance ou album
			}
		});		
	}
	
	/**
	 * Load albums for a gallery instance.
	 * @param instanceId
	 */
	public void loadAlbums(final ApplicationInstanceDTO instance) {
		ServicesLocator.serviceGallery.getAllAlbums(instance.getId(), new AsyncCallback<List<AlbumDTO>>() {
			@Override
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
			}
			@Override
			public void onSuccess(List<AlbumDTO> result) {
				EventBus.getInstance().fireEvent(new GalleryNewInstanceLoadedEvent(instance, result));				
			}			
		});
	}

	/**
	 * Store settings.
	 */
	@Override
	public void saveSettings(final GallerySaveSettingsEvent event) {
		Database.open();		
		final Entity<GallerySettings> settingsEntity = GWT.create(GallerySettings.class);
		final Collection<GallerySettings> settings = settingsEntity.all();		
		Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {
			@Override
			public void onSuccess() {
				settings.destroyAll(new com.gwtmobile.persistence.client.Callback() {
					public void onSuccess() {						
						Persistence.flush();
						final Entity<GallerySettings> settingsEntity = GWT.create(GallerySettings.class);				
						Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {			
							public void onSuccess() {
								final GallerySettings settings = settingsEntity.newInstance();
								settings.setSelectedGalleryId(event.getGallery().getId());
								settings.setSelectedAlbumId(event.getAlbumId());
								settings.setSelectedGalleryLabel(event.getGallery().getLabel());
								Persistence.flush();
							}
						});				
					}
				});				
			}		
		});
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractGalleryControllerEvent.TYPE, this);
		EventBus.getInstance().removeHandler(AbstractNavigationEvent.TYPE, this);
	}

	@Override
	public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {		
		loadAlbums(event.getInstance());		
	}

	/**
	 * Delete a local picture.
	 */
	@Override
	public void deleteLocalPicture(final DeleteLocalPictureEvent event) {
		Database.open();
		final Entity<Picture> pictureEntity = GWT.create(Picture.class);
		final Collection<Picture> pics = pictureEntity.all();
		pics.list(new CollectionCallback<Picture>() {

			@Override
			public void onSuccess(Picture[] results) {					
				for (int i = 0; i < results.length; i++) {
					Picture picture = results[i];					
					if (picture.getId().equals(event.getId())) {
						pics.remove(picture);
						EventBus.getInstance().fireEvent(new DeletedLocalPictureEvent(results.length==1));						
						break;
					}									
				}								
			}							
		});		
	}

	/**
	 * Load local pictures on device.
	 */
	@Override
	public void loadLocalPictures(LoadLocalPicturesEvent event) {
		Database.open();
		final Entity<Picture> pictureEntity = GWT.create(Picture.class);
		final Collection<Picture> pictures = pictureEntity.all();
		pictures.count(new ScalarCallback<Integer>() {					
			@Override
			public void onSuccess(final Integer count) {
				if (count == 0) {					
					EventBus.getInstance().fireEvent(new GalleryLocalPicturesLoadedEvent(null));
				} else {
					Notification.activityStart();
					pictures.list((new CollectionCallback<Picture>(){
	
						@Override
						public void onSuccess(Picture[] pictures) {
							EventBus.getInstance().fireEvent(new GalleryLocalPicturesLoadedEvent(pictures));							
						}						
					}));				
				}
			}
		});		
	}
	
	private void uploadPicture(Picture picture, final String idGallery, final String idAlbum) {
		ServicesLocator.serviceGallery.uploadPicture(picture.getName(), picture.getData(), idGallery, idAlbum, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
			}

			@Override
			public void onSuccess(Void result) {
				localsPicturesList.remove(localsPictures[indexNextPictureToUpload]);
				indexNextPictureToUpload++;
				
				EventBus.getInstance().fireEvent(new GalleryPictureUploadedEvent());
				
				if (localsPictures.length > indexNextPictureToUpload) {
					uploadPicture(localsPictures[indexNextPictureToUpload], idGallery, idAlbum);	
				} else {
					// send message "end upload"
					EventBus.getInstance().fireEvent(new GalleryEndUploadEvent());
				}				
			}
		});
	}

	/**
	 * Send local pictures to remote album.
	 */
	@Override
	public void syncPictures(final SyncPicturesEvent event) {
		
		/*
		// For optimal performances
		FileReader reader = File.newReaderInstance();
		reader.readAsDataURL("");
		reader.onLoadEnd(callback);
		*/	
		
		indexNextPictureToUpload = 0;
		Database.open();
		final Entity<Picture> pictureEntity = GWT.create(Picture.class);
		localsPicturesList = pictureEntity.all();
		localsPicturesList.count(new ScalarCallback<Integer>() {
			
			@Override
			public void onSuccess(final Integer count) {
				EventBus.getInstance().fireEvent(new GalleryStartingUploadEvent(count));
				if (count > 0) {				
					localsPicturesList.list(new CollectionCallback<Picture>() {
						@Override
						public void onSuccess(Picture[] results) {
							localsPictures = results;
							uploadPicture(localsPictures[indexNextPictureToUpload], event.getIdGallery(), event.getIdAlbum());							
						}					
					});
				}
			}
		});	
	}

	@Override
	public void takePicture(TakePictureEvent takePictureEvent) {
		// TODO Auto-generated method stub
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
						DateTimeFormat fmt = DateTimeFormat.getFormat("dd-MM-yyyy_HH:mm:ss");
						pic.setName("mobil_" + fmt.format(new Date()));
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
}
