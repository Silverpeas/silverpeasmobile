package com.silverpeas.mobile.client.pages.gallery;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.persistence.client.Collection;
import com.gwtmobile.persistence.client.CollectionCallback;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.gwtmobile.phonegap.client.Camera;
import com.gwtmobile.phonegap.client.Notification;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.Transition;
import com.silverpeas.mobile.client.common.Database;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.components.icon.Icon;
import com.silverpeas.mobile.client.pages.gallery.browser.PicturePage;
import com.silverpeas.mobile.client.persist.Picture;

public class GalleryPage extends Page {

	private static GalleryPageUiBinder uiBinder = GWT.create(GalleryPageUiBinder.class);
	@UiField protected Icon takePicture, local, sync, setup;	
	
	private static int nbPictures;
	private static int ratioPicture;
	private static boolean uploading, stopScheduler;
	
	private PicturePage picturePage = new PicturePage();
	
	interface GalleryPageUiBinder extends UiBinder<Widget, GalleryPage> {
	}

	public GalleryPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("takePicture")
	void takePicture(ClickEvent e){
		Camera.Options options = new Camera.Options();
		options.quality(50);	
		
		Camera.getPicture(new Camera.Callback() {			
			public void onSuccess(final String imageData) {				
				Database.open();		
				final Entity<Picture> pictureEntity = GWT.create(Picture.class);				
				Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {			
					public void onSuccess() {
						final Picture pic = pictureEntity.newInstance();
						pic.setData(imageData);
						Persistence.flush();
					}
				});
			}

			public void onError(String message) {
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(message)));			
			}
		}, options);
	}
	
	
	@UiHandler("local")
	void localPictures(ClickEvent e){
		Database.open();
		final Entity<Picture> pictureEntity = GWT.create(Picture.class);
		final Collection<Picture> pictures = pictureEntity.all();
		picturePage.clear();
		pictures.count(new ScalarCallback<Integer>() {
					
			@Override
			public void onSuccess(final Integer count) {
				Notification.activityStart();
				nbPictures = 0;
				
				pictures.forEach(new ScalarCallback<Picture>() {			
					@Override
					public void onSuccess(Picture result) {
						Image picture = new Image("data:image/jpg;base64,"+result.getData());
						picture.setSize("100%", "100%");
						picturePage.addPicture(picture);
						nbPictures++;
						if (count == nbPictures) {
							Notification.activityStop();
							goTo(picturePage, Transition.SLIDE);
						}						
					}
				});				
			}
		});		
	}
	
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
}
