package com.silverpeas.mobile.client.apps.media.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.media.events.app.MediasLoadMediaItemsEvent;
import com.silverpeas.mobile.client.apps.media.resources.MediaMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.navigation.UrlUtils;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.rebind.ConfigurationProvider;
import com.silverpeas.mobile.client.resources.ApplicationMessages;


/**
 * @author: svu
 */
public class AddMediaButton extends Composite {
    interface AddMediaButtonUiBinder extends UiBinder<Widget, AddMediaButton> {
    }

    @UiField FileUpload file;
    @UiField FormPanel upload;
    @UiField Hidden componentId, albumId;
    @UiField Anchor link;
    @UiField(provided = true) protected MediaMessages msg = null;

    private String instanceIdValue, albumIdValue;
    private ApplicationMessages globalMsg = GWT.create(ApplicationMessages.class);

    private static AddMediaButtonUiBinder uiBinder = GWT.create(AddMediaButtonUiBinder.class);

    public AddMediaButton() {
        msg = GWT.create(MediaMessages.class);
        initWidget(uiBinder.createAndBindUi(this));
        file.getElement().setAttribute("accept", "audio/*, video/*, image/*");
        file.getElement().setAttribute("multiple", "multiple");
        upload.setEncoding(FormPanel.ENCODING_MULTIPART);
        upload.getElement().getStyle().setDisplay(Style.Display.NONE);

        upload.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

            @Override
            public void onSubmitComplete(final FormPanel.SubmitCompleteEvent submitCompleteEvent) {
                String r = submitCompleteEvent.getResults();
                if (r.contains("HTTP 413")) {
                    Notification.alert(msg.maxUploadError());
                } else if (r.contains("HTTP 415")) {
                    Notification.alert(msg.mediaNotSupportedError());
                }
                EventBus.getInstance().fireEvent(new MediasLoadMediaItemsEvent(instanceIdValue, albumIdValue));
            }
        });
    }

    public void init(String instanceId, String albumId) {
        this.instanceIdValue = instanceId;
        this.albumIdValue = albumId;
        this.componentId.setValue(instanceId);
        this.albumId.setValue(albumId);

        String url = UrlUtils.getUploadLocation();
        url +=  "MediaAction";
        upload.setAction(url);
    }


    @UiHandler("file")
    void upload(ChangeEvent event) {
        upload.submit();
        Notification.activityStart();
        upload.reset();
    }

    @UiHandler("link")
    void upload(ClickEvent event) {
        if (OfflineHelper.isOffLine() == false ) {
            clickOnInputFile(file.getElement());
        } else {
            Notification.alert(globalMsg.needToBeOnline());
        }

    }

    private static native void clickOnInputFile(Element elem) /*-{
        elem.click();
    }-*/;

}