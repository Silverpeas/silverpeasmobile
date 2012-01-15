package com.silverpeas.mobile.client.pages.gallery.browser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.persistence.client.Collection;
import com.gwtmobile.persistence.client.CollectionCallback;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.phonegap.client.Notification;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Slide;
import com.gwtmobile.ui.client.widgets.SlidePanel;
import com.silverpeas.mobile.client.common.Database;
import com.silverpeas.mobile.client.persist.Picture;

public class PicturePage extends Page {

	private static PicturePageUiBinder uiBinder = GWT.create(PicturePageUiBinder.class);
	private static int nbPictures = 0;
	@UiField SlidePanel content;
	@UiField InlineHTML number;

	interface PicturePageUiBinder extends UiBinder<Widget, PicturePage> {
	}
	
	public class ImageClickHandler implements ClickHandler {

		@Override
		public void onClick(final ClickEvent event) {
			Notification.confirm("Delete picture ?", new Notification.ConfirmCallback() {		
				public void onComplete(int selection) {
					if (selection == 1) {	
						
						// delete picture in database
						Database.open();
						final Entity<Picture> pictureEntity = GWT.create(Picture.class);
						final Collection<Picture> pics = pictureEntity.all();
						pics.list(new CollectionCallback<Picture>() {

							@Override
							public void onSuccess(Picture[] results) {
								int index = content.getCurrentSlideIndex();
								Slide currentSlide = content.getSlide(index);
									
								for (int i = 0; i < results.length; i++) {
									Picture picture = results[i];					
									String id = currentSlide.getElement().getId();
									if (picture.getId().equals(id)) {
										pics.remove(picture);
										content.remove(currentSlide);										
										if (results.length == 1) {
											goBack(null);
										} else {
											content.next();
										}
										nbPictures--;
										displayPicturesNumber();
										break;
									}									
								}								
							}							
						});
					}				
				}
			}, "Confirm", "OK,Cancel");
		}
	}

	public PicturePage() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setPictures(Picture[] pictures) {
		nbPictures = pictures.length;
		for (int i = 0; i < pictures.length; i++) {
			Picture picture = pictures[i];
			Image image = new Image("data:image/jpg;base64,"+picture.getData());
			image.setSize("100%", "100%");
			image.addClickHandler(new ImageClickHandler());
			Slide s = new Slide();
			s.getElement().setId(picture.getId());
			s.add(image);
			content.add(s);
		}
		displayPicturesNumber();			
	}

	private void displayPicturesNumber() {
		number.setHTML("&nbsp;("+nbPictures+")");
		
	}
}
