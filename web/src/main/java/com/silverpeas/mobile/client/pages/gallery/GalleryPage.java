package com.silverpeas.mobile.client.pages.gallery;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.persistence.client.Collection;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.gwtmobile.phonegap.client.Camera;
import com.gwtmobile.phonegap.client.Notification;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.Transition;
import com.gwtmobile.ui.client.widgets.Button;
import com.silverpeas.mobile.client.common.Database;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.pages.gallery.browser.PicturePage;
import com.silverpeas.mobile.client.persist.Picture;

public class GalleryPage extends Page {

	private static GalleryPageUiBinder uiBinder = GWT.create(GalleryPageUiBinder.class);
	@UiField Button takePicture, local, sync;	
	
	private static int nbPictures;
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
		//options.sourceType(Camera.SourceType.PHOTOLIBRARY);
		
		Camera.getPicture(new Camera.Callback() {			
			public void onSuccess(final String imageData) {
				
				//Network.getConnectionType().isWifi()
				
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
		// TODO	
	}
}
