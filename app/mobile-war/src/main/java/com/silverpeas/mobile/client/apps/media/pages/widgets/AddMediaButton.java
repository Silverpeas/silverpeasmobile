package com.silverpeas.mobile.client.apps.media.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.media.events.app.MediasLoadMediaItemsEvent;
import com.silverpeas.mobile.client.apps.media.resources.MediaMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.navigation.UrlUtils;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.resources.ApplicationMessages;


/**
 * @author: svu
 */
public class AddMediaButton extends Composite {
    interface AddMediaButtonUiBinder extends UiBinder<Widget, AddMediaButton> {
    }

    @UiField FileUpload file;
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
        file.getElement().setAttribute("length", "40");
        file.getElement().getStyle().setDisplay(Style.Display.NONE);
    }

    public void init(String instanceId, String albumId) {
        this.instanceIdValue = instanceId;
        this.albumIdValue = albumId;
    }

    @UiHandler("file")
    void upload(ChangeEvent event) {
      Notification.activityStart();
      String url = UrlUtils.getUploadLocation();
      url +=  "MediaAction";
      upload(this, file.getElement(), instanceIdValue, albumIdValue, url, SpMobil.getUserToken());
    }

    public void mediaUploadedSuccessfully() {
      EventBus.getInstance().fireEvent(new MediasLoadMediaItemsEvent(instanceIdValue, albumIdValue));
    }

    public void mediaNotUploaded(int codeError) {
      GWT.log("error " + codeError);
      if (codeError == 413) {
        Notification.alert(msg.maxUploadError());
      } else if (codeError == 415) {
        Notification.alert(msg.mediaNotSupportedError());
      }
      Notification.activityStop();
    }

    @UiHandler("link")
    void upload(ClickEvent event) {
        if (OfflineHelper.isOffLine() == false ) {
            clickOnInputFile(file.getElement());
        } else {
            Notification.alert(globalMsg.needToBeOnline());
        }

    }

    private static native void upload(AddMediaButton button, Element input, String componentId, String albumId, String url, String token) /*-{
      var xhr = new XMLHttpRequest();
      var fd = new FormData();
      xhr.open("POST", url, false);
      xhr.setRequestHeader("X-Silverpeas-Session", token);
      xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
          // Every thing ok, file uploaded
          button.@com.silverpeas.mobile.client.apps.media.pages.widgets.AddMediaButton::mediaUploadedSuccessfully()();
        } else {
          button.@com.silverpeas.mobile.client.apps.media.pages.widgets.AddMediaButton::mediaNotUploaded(I)(xhr.status);
        }
      };
      fd.append("componentId", componentId);
      fd.append("albumId", albumId);
      for(var i = 0; i < input.files.length ; i++) {
        fd.append("upload_file"+i, input.files[i]);
      }
      xhr.send(fd);
    }-*/;

    private static native void clickOnInputFile(Element elem) /*-{
        elem.click();
    }-*/;

}