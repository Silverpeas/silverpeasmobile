package com.silverpeas.mobile.client.pages.gallery.browser.remote;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.DropDownList;

public class GalleryRemoteBrowser extends Page {

	private static GalleryRemoteBrowserUiBinder uiBinder = GWT.create(GalleryRemoteBrowserUiBinder.class);
	
	@UiField
	DropDownList galleries, albums;

	interface GalleryRemoteBrowserUiBinder extends
			UiBinder<Widget, GalleryRemoteBrowser> {
	}

	public GalleryRemoteBrowser() {
		initWidget(uiBinder.createAndBindUi(this));
		
		galleries.getElement().getElementsByTagName("select").getItem(0).getStyle().setDisplay(Display.BLOCK);
		galleries.getElement().getElementsByTagName("select").getItem(0).getStyle().setWidth(100, Unit.PCT);
		albums.getElement().getElementsByTagName("select").getItem(0).getStyle().setDisplay(Display.BLOCK);
		albums.getElement().getElementsByTagName("select").getItem(0).getStyle().setWidth(100, Unit.PCT);
	}
}
