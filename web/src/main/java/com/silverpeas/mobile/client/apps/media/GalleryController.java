package com.silverpeas.mobile.client.apps.media;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class GalleryController implements Controller, NavigationEventHandler {
	
	// Temporary data for synch
	//private static List<Picture> localsPictures;
	
	public GalleryController() {
		super();
		//EventBus.getInstance().addHandler(AbstractGalleryControllerEvent.TYPE, this);
		EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
	}

	/**
	 * Load stored settings and send settings on bus.
	 */

	public void loadSettings(/*GalleryLoadSettingsEvent event*/) {
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {
			String dataItem = storage.getItem("gallerySettings");			
			if (dataItem != null) {
				//final GallerySettings settings = GallerySettings.getInstance(dataItem);
				
				/*ServicesLocator.serviceMedia.getAllAlbums(settings.getSelectedGalleryId(), new AsyncCallback<List<AlbumDTO>>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
					}
					@Override
					public void onSuccess(List<AlbumDTO> albums) {
						EventBus.getInstance().fireEvent(new GalleryLoadedSettingsEvent(settings, albums));
					}			
				});*/
				//TODO :type filter text gestion suppression instance ou album
			}
		}		
	}
	
	/**
	 * Load albums for a gallery instance.
	 * @param instance
	 */
	public void loadAlbums(final ApplicationInstanceDTO instance) {
		/*ServicesLocator.serviceMedia.getAllAlbums(instance.getId(), new AsyncCallback<List<AlbumDTO>>() {
			@Override
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
			}
			@Override
			public void onSuccess(List<AlbumDTO> result) {
				EventBus.getInstance().fireEvent(new GalleryNewInstanceLoadedEvent(instance, result));				
			}			
		});*/
	}

	/**
	 * Store settings.
	 */

	public void saveSettings(/*final GallerySaveSettingsEvent event*/) {
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {
			//GallerySettings settings = new GallerySettings(event.getAlbumId(), event.getGallery().getId(), event.getGallery().getLabel());
			//storage.setItem("gallerySettings", settings.toJson());
		}		
	}

	@Override
	public void stop() {
		//EventBus.getInstance().removeHandler(AbstractGalleryControllerEvent.TYPE, this);
		EventBus.getInstance().removeHandler(AbstractNavigationEvent.TYPE, this);
	}

	@Override
	public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {		
		loadAlbums(event.getInstance());		
	}

	/**
	 * Delete a local picture.
	 */

	public void deleteLocalPicture(/*final DeleteLocalPictureEvent event*/) {
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {			
			StorageMap storageMap = new StorageMap(storage);
			//storageMap.remove("picture"+event.getId());
		}		
	}

	/**
	 * Load local pictures on device.
	 */
	public void loadLocalPictures(/*LoadLocalPicturesEvent event*/) {
	/*
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {			
			StorageMap storageMap = new StorageMap(storage);
			int i = 0;
			final List<Picture> pictures = new ArrayList<Picture>();
			while (i < storage.getLength()) {
				if (storageMap.containsKey("picture"+i)) {
					pictures.add(Picture.getInstance(storageMap.get("picture"+i)));
				}
				i++;
			}
			if (i == 0) {
				EventBus.getInstance().fireEvent(new GalleryLocalPicturesLoadedEvent(null));
			} else {
				Notification.activityStart();
				EventBus.getInstance().fireEvent(new GalleryLocalPicturesLoadedEvent(pictures));
			}						
		}		*/
	}
	
	/**
	 * Effective upload and remove file picture on success upload.

	 * @param idGallery
	 * @param idAlbum
	 */
	private void sendAndRemovePicture(/*final Picture picture,*/ final String idGallery, final String idAlbum) {
		/*ServicesLocator.serviceMedia.uploadPicture(picture.getName(), picture.getUri(), idGallery, idAlbum, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
			}

			@Override
			public void onSuccess(Void result) {
				localsPictures.remove(0);
				Storage storage = Storage.getLocalStorageIfSupported();
				if (storage != null) {			
					StorageMap storageMap = new StorageMap(storage);
					storageMap.remove("picture"+picture.getId());
				}
				EventBus.getInstance().fireEvent(new GalleryPictureUploadedEvent());
				
				if (localsPictures.size() > 0) {
					sendAndRemovePicture(localsPictures.get(0), idGallery, idAlbum);	
				} else {
					// send message "end upload"
					EventBus.getInstance().fireEvent(new GalleryEndUploadEvent());
				}
			}
		});*/
	}

	/**
	 * Send local pictures to remote album.
	 */
	public void syncPictures(/*final SyncPicturesEvent event*/) {
				
		/*Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {			
			StorageMap storageMap = new StorageMap(storage);
			int i = 0;
			final List<Picture> pictures = new ArrayList<Picture>();
			while (i < storage.getLength()) {
				if (storageMap.containsKey("picture"+i)) {
					pictures.add(Picture.getInstance(storageMap.get("picture"+i)));
				}
				i++;
			}
			localsPictures = pictures;
			
			EventBus.getInstance().fireEvent(new GalleryStartingUploadEvent(pictures.size()));
			sendAndRemovePicture(localsPictures.get(0), event.getIdGallery(), event.getIdAlbum());	
		}*/
	}

	/**
	 * Take a picture from camera device.
	 */
	public void takePicture(/*final TakePictureEvent takePictureEvent*/) {
		/*Notification.activityStart();
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage != null) {			
			StorageMap storageMap = new StorageMap(storage);
			int i = 0;
			while(storageMap.containsKey("picture"+i)) {				
				i++;
			}
			
			Picture pic = new Picture();
			pic.setId(String.valueOf(i));
			DateTimeFormat fmt = DateTimeFormat.getFormat("ddMMyyyy-HHmmss");
			pic.setName("mobil_" + fmt.format(new Date()));
			pic.setUri(takePictureEvent.getImageData());			
			storage.setItem("picture"+pic.getId(), pic.toJson());			
		}
		Notification.activityStop();*/
	}

	/**
	 * Load remote album pictures.
	 */

	public void loadRemotePictures(/*RemotePicturesLoadEvent event*/) {
		/*ServicesLocator.serviceMedia.getAllPictures(event.getGalleryId(), event.getAlbumId(), new AsyncCallback<List<PhotoDTO>>() {
			@Override
			public void onSuccess(List<PhotoDTO> result) {
				EventBus.getInstance().fireEvent(new RemotePictureLoadedEvent(result));
			}

			@Override
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
			}
		});*/
	}

	/**
	 *
	 */

	public void loadRemotePreviewPicture(/*LoadRemotePreviewPictureEvent event*/) {
		Notification.activityStart();
		/*ServicesLocator.serviceMedia.getOriginalPicture(event.getGalleryId(), event.getPhotoId(), new AsyncCallback<PhotoDTO>() {
			@Override
			public void onSuccess(PhotoDTO result) {
				EventBus.getInstance().fireEvent(new PictureViewLoadedEvent(result));
				Notification.activityStop();
			}

			@Override
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
				Notification.activityStop();
			}
		});*/

	}
}
