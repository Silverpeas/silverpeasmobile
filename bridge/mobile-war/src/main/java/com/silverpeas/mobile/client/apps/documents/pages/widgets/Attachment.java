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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.navigation.UrlUtils;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.documents.AttachmentDTO;

public class Attachment extends Composite {

    private static AttachmentUiBinder uiBinder = GWT.create(AttachmentUiBinder.class);
    @UiField Anchor link;
    @UiField SpanElement size, name;
    @UiField ImageElement icon;

    protected DocumentsResources ressources = null;
    private DocumentsMessages msg = null;
    private ApplicationMessages globalMsg = null;
    private boolean clicked = false;
    private AttachmentDTO attachement;

    interface AttachmentUiBinder extends UiBinder<Widget, Attachment> {
    }

    public Attachment() {
        msg = GWT.create(DocumentsMessages.class);
        globalMsg = GWT.create(ApplicationMessages.class);
        ressources = GWT.create(DocumentsResources.class);
        ressources.css().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("link")
    protected void onClick(ClickEvent event) {
        if (OfflineHelper.isOffLine()) {
            Notification.alert(globalMsg.needToBeOnline());
            return;
        }
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
        Image img = null;
        String sizeValue;
        if (attachement.getSize() < 1024*1024) {
            sizeValue = String.valueOf(attachement.getSize()/1024);
            size.setInnerHTML(msg.sizeK(sizeValue));
        } else {
            sizeValue = String.valueOf(attachement.getSize()/(1024*1024));
            size.setInnerHTML(msg.sizeM(sizeValue));
        }
        name.setInnerHTML(attachement.getTitle());
        if (attachement.getType().contains("msword")) {
            img = new Image(ressources.msword());
        } else if (attachement.getType().contains("sheet")) {
            img = new Image(ressources.msexcel());
        } else if (attachement.getType().contains("pdf")) {
            img = new Image(ressources.pdf());
        } else if (attachement.getType().contains("image")) {
            img = new Image(ressources.image());
        } else if (attachement.getType().contains("presentation")) {
            img = new Image(ressources.mspowerpoint());
        }
        else {
            img = new Image(ressources.unknown());
        }
        icon.getParentElement().replaceChild(img.getElement(), icon);
    }

    private void clickAction() {
        try {
            String url = UrlUtils.getLocation();
            url += "spmobil/Attachment";
            url = url + "?id=" + attachement.getId() + "&lang=" + attachement.getLang();
            link.setHref(url);
            link.setTarget("_self");
            link.fireEvent(new ClickEvent() {});
            link.getElement().setAttribute("download", attachement.getTitle());
        } catch(JavaScriptException e) {
            Notification.alert(e.getMessage());
        }
    }
}
