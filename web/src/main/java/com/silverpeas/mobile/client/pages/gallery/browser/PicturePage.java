package com.silverpeas.mobile.client.pages.gallery.browser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.phonegap.client.Notification;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Slide;
import com.gwtmobile.ui.client.widgets.SlidePanel;

public class PicturePage extends Page implements ClickHandler {

	private static PicturePageUiBinder uiBinder = GWT.create(PicturePageUiBinder.class);
	@UiField SlidePanel content;

	interface PicturePageUiBinder extends UiBinder<Widget, PicturePage> {
	}

	public PicturePage() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void addPicture(Image picture) {
		picture.addClickHandler(this);		
		Slide s = new Slide();
		s.add(picture);
		content.add(s);		
	}
	
	public void clear() {
		content.clear();
	}

	@Override
	public void onClick(ClickEvent event) {
		Notification.confirm("Delete picture ?", new Notification.ConfirmCallback() {		
			public void onComplete(int selection) {
				if (selection == 1) {
					// TODO : delete picture in database
				}				
			}
		}, "Confirm", "OK,Cancel");	
	}
}
