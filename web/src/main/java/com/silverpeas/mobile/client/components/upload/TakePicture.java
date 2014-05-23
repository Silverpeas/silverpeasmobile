package com.silverpeas.mobile.client.components.upload;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.gwtmobile.ui.client.widgets.VerticalPanel;
import com.silverpeas.mobile.client.apps.gallery.events.controller.TakePictureEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.file.FileMgr;
import com.silverpeas.mobile.client.common.file.FileMgr.*;
import com.silverpeas.mobile.client.resources.ApplicationResources;

public class TakePicture extends Composite implements HasText, HasChangeHandlers, ChangeHandler {	
	private ChangeHandler handler = null;
	private FileUpload file = null;
	private ApplicationResources res =  GWT.create(ApplicationResources.class);
	private Label label = new Label();
	
	@UiConstructor
	public TakePicture(ImageResource icon) {
		super();
		VerticalPanel grid = new VerticalPanel();
		label.setHorizontalAlignment(Label.ALIGN_CENTER);
		grid.setSecondaryStyle(res.css().icon());
		
		SimplePanel widget = new SimplePanel();		
		widget.getElement().getStyle().setWidth(icon.getWidth(), Unit.PX);
		widget.getElement().getStyle().setHeight(icon.getHeight(), Unit.PX);
		widget.getElement().getStyle().setBackgroundImage("url('"+icon.getSafeUri().asString()+"')");
		
		
		file = new FileUpload();
		file.getElement().setId("file-upload");
		file.getElement().setAttribute("accept", "image/*");
		file.getElement().setAttribute("capture", "camera");
		
		file.getElement().getStyle().setWidth(icon.getWidth(), Unit.PX);
		file.getElement().getStyle().setHeight(icon.getHeight(), Unit.PX);
		file.getElement().getStyle().setOpacity(0);
		widget.add(file);
		
		
		grid.add(widget);		
		grid.add(label);
		
		initWidget(grid);
	}

	public String getText() {
		return label.getText();
	}

	public void setText(String text) {
		label.setText(text);
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		this.handler = handler;
		file.addChangeHandler(handler);	
		return new TakePictureHandlerRegistration();
	}
	
	/**
	 * Permet de se désabonner au clique sur l'icone.
	 * @author svuillet
	 */
	public class TakePictureHandlerRegistration implements HandlerRegistration {		
		public TakePictureHandlerRegistration() {
			super();
		}

		public void removeHandler() {
			handler = null;			
		}
	}

	@Override
	public void onChange(ChangeEvent event) {
		if (handler != null) handler.onChange(event);
		
	}
	
	public void getImageData() {				
		FileReader f = FileMgr.newFileReader();		
		f.onLoad(new EventCallback() {			
			@Override
			public void onEvent(com.silverpeas.mobile.client.common.file.FileMgr.Event evt) {				
				// send message to controller to store picture in local database
				EventBus.getInstance().fireEvent(new TakePictureEvent(evt.getTarget().getResult()));				
			}			
		});
		f.readAsDataURL((File)getImageFile());		
	}
	
	private native JavaScriptObject getImageFile() /*-{
		var f = $doc.getElementById("file-upload");		
		var files = f.files, file;
		if (files && files.length > 0) {
        	file = files[0];
    	}
    	
		return file;
	}-*/;
}
