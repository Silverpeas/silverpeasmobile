package com.silverpeas.mobile.client.apps.documents.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.shared.dto.documents.AttachmentDTO;

public class Attachment extends Composite {

	private static AttachmentUiBinder uiBinder = GWT.create(AttachmentUiBinder.class);
	@UiField Anchor link;
	@UiField SpanElement size, name;
	@UiField ImageElement icon;
	
	protected DocumentsResources ressources = null;
	private boolean clicked = false;
	private AttachmentDTO attachement;
	
	interface AttachmentUiBinder extends UiBinder<Widget, Attachment> {
	}

	public Attachment() {
		ressources = GWT.create(DocumentsResources.class);		
		ressources.css().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));		
	}

	@UiHandler("link")
	protected void onClick(ClickEvent event) {
		if (!clicked) {
			clicked = true;
			clickAction();
			Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
				@Override
				public boolean execute() {
					clicked = false;
					return false;
				}}, 400);
		}
	}
	
	public void setAttachment(AttachmentDTO attachmentDTO) {
		this.attachement = attachmentDTO;
		render();
	}
	
	private void render() {
		if (attachement.getSize() < 1024*1024) {
			size.setInnerHTML(attachement.getSize()/1024 + " Ko");	
		} else {
			size.setInnerHTML(attachement.getSize()/(1024*1024) + " Mo");
		}		
		name.setInnerHTML(attachement.getTitle());		
		if (attachement.getType().contains("msword")) {
			icon.setClassName(ressources.css().msword());			
		} else if (attachement.getType().contains("sheet")) {
			icon.setClassName(ressources.css().msexcel());
		} else if (attachement.getType().contains("pdf")) {
			icon.setClassName(ressources.css().pdf());
		} else if (attachement.getType().contains("image")) {			
			icon.setClassName(ressources.css().image());
		} else if (attachement.getType().contains("presentation")) {
			icon.setClassName(ressources.css().mspowerpoint());
		}
		else {
			//icon.setSrc("/silverpeas/util/icons/fileType/unknown.gif");			
			icon.setClassName(ressources.css().unknown());
		}		
	}
	
	private void clickAction() {
		try {
			String url = Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/spmobile/spmobil/Attachment";					
			url = url + "?id=" + attachement.getId() + "&instanceId=" + attachement.getInstanceId() + "&lang=" + attachement.getLang()  + "&userId=" + attachement.getUserId();
			
			link.setHref(url);
			link.setTarget("_self");
			link.fireEvent(new ClickEvent() {});

		} catch(JavaScriptException e) {
			Notification.alert(e.getMessage(), null, "error", "ok");
		}
	}
}
