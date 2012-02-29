package com.silverpeas.mobile.client.apps.gallery.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.phonegap.client.Notification;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.Slide;
import com.gwtmobile.ui.client.widgets.SlidePanel;
import com.silverpeas.mobile.client.apps.gallery.events.controller.DeleteLocalPictureEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.local.AbstractLocalPicturesPageEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.local.DeletedLocalPictureEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.local.LocalPicturesPageEventHandler;
import com.silverpeas.mobile.client.apps.gallery.persistances.Picture;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryMessages;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryResources;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;

/**
 * Local pictures browser on device.
 * @author svuillet
 */
public class PicturePage extends Page implements View, LocalPicturesPageEventHandler, ValueChangeHandler<Boolean> {

	private static PicturePageUiBinder uiBinder = GWT.create(PicturePageUiBinder.class);
	private static int nbPictures = 0;
	@UiField SlidePanel content;
	@UiField InlineHTML number;
	@UiField Button prevButton, nextButton, delButton;
	@UiField(provided = true) protected GalleryMessages msg = null;
	@UiField(provided = true) protected GalleryResources ressources = null;

	interface PicturePageUiBinder extends UiBinder<Widget, PicturePage> {
	}

	/**
	 * Construct page.
	 */
	public PicturePage() {
		ressources = GWT.create(GalleryResources.class);		
		ressources.css().ensureInjected();
		msg = GWT.create(GalleryMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
		int size = delButton.getText().length();
		delButton.setWidth(size + "em");
		EventBus.getInstance().addHandler(AbstractLocalPicturesPageEvent.TYPE, this);
		content.addValueChangeHandler(this);
	}
	
	/**
	 * Set pictures to browse.
	 * @param pictures
	 */
	public void setPictures(Picture[] pictures) {
		nbPictures = pictures.length;
		for (int i = 0; i < pictures.length; i++) {
			Picture picture = pictures[i];
			Image image = new Image("data:image/jpg;base64,"+picture.getData());			
			image.setWidth("100%");			
			Slide s = new Slide();
			s.addStyleName(ressources.css().localPicture());			
			s.getElement().setId(picture.getId());
			s.add(image);		
			content.add(s);						
		}		
		displayPicturesNumber();			
	}

	private void displayPicturesNumber() {
		int index = content.getCurrentSlideIndex() + 1;
		number.setHTML("&nbsp;"+index+"/"+nbPictures);		
	}
	
	/**
	 * Invoke delete order. 
	 */
	private void deletePicture() {
		Notification.confirm(msg.localPicture_deleteConfirmation(), new Notification.ConfirmCallback() {		
			public void onComplete(int selection) {
				if (selection == 1) {					
					int index = content.getCurrentSlideIndex();
					Slide currentSlide = content.getSlide(index);
					EventBus.getInstance().fireEvent(new DeleteLocalPictureEvent(currentSlide.getElement().getId()));
				}				
			}
		}, "Confirm", "OK,Cancel");
	}
	
	@UiHandler("delButton")
	void delPicture(ClickEvent e){
		deletePicture();
	}
	
	@UiHandler("prevButton")
	void previousPicture(ClickEvent e){
		content.previous();	
	}
	
	@UiHandler("nextButton")
	void nextPicture(ClickEvent e){
		content.next();	
	}

	/**
	 * Picture has been deleted.
	 */
	@Override
	public void onDeletedLocalPicture(DeletedLocalPictureEvent event) {
		int index = content.getCurrentSlideIndex();
		Slide currentSlide = content.getSlide(index);
		
		content.remove(currentSlide);
		if (event.isNoMorePicture()) {
			goBack(null);
		} else {
			content.next();
		}
		nbPictures--;
		displayPicturesNumber();		
	}
	
	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractLocalPicturesPageEvent.TYPE, this);		
	}
	
	@Override
	public void goBack(Object returnValue) {
		stop();				
		super.goBack(returnValue);
	}

	/**
	 * Refresh current picture position on picture change.
	 */
	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		displayPicturesNumber();		
	}
}
