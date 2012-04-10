package com.silverpeas.mobile.client.apps.documents.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.documents.AttachmentDTO;

public class AttachmentWidget extends Composite {

	private static AttachmentWidgetUiBinder uiBinder = GWT.create(AttachmentWidgetUiBinder.class);
	private AttachmentDTO attachement;
	
	@UiField(provided = true) protected DocumentsMessages msg = null;
	@UiField(provided = true) protected ApplicationMessages globalMsg = null;
	@UiField(provided = true) protected DocumentsResources ressources = null;
	
	@UiField Label title, size, date, icon;	

	
	
	interface AttachmentWidgetUiBinder extends UiBinder<Widget, AttachmentWidget> {
	}
	
	public AttachmentWidget() {
		ressources = GWT.create(DocumentsResources.class);		
		ressources.css().ensureInjected();
		msg = GWT.create(DocumentsMessages.class);
		globalMsg = GWT.create(ApplicationMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("item")
	void download(ClickEvent event) {
		Window.open(attachement.getUrl(), "_blank", "");
	}

	public void setAttachment(AttachmentDTO attachmentDTO) {
		this.attachement = attachmentDTO;
		render();
	}

	private void render() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("dd MMMM yyyy");		
		title.setText(attachement.getTitle());	
		size.setText(attachement.getSize()/1024 + " Ko");
		date.setText(fmt.format(attachement.getCreationDate()));		
		if (attachement.getType().contains("msword")) {
			icon.setStylePrimaryName(ressources.css().msword());
		} else if (attachement.getType().contains("sheet")) {
			icon.setStylePrimaryName(ressources.css().msexcel());
		}
		
	}
}
