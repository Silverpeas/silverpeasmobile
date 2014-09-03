package com.silverpeas.mobile.client.apps.media.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.media.events.app.MediasLoadMediaItemsEvent;
import com.silverpeas.mobile.client.apps.media.resources.MediaMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;


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

  private static AddMediaButtonUiBinder uiBinder = GWT.create(AddMediaButtonUiBinder.class);

  public AddMediaButton() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(MediaMessages.class);

    file.getElement().setAttribute("accept", "image/*");
    file.getElement().setAttribute("multiple", "multiple");
    upload.setEncoding(FormPanel.ENCODING_MULTIPART);
    upload.getElement().getStyle().setDisplay(Style.Display.NONE);

    upload.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

      @Override
      public void onSubmitComplete(final FormPanel.SubmitCompleteEvent submitCompleteEvent) {
        EventBus.getInstance().fireEvent(new MediasLoadMediaItemsEvent(instanceIdValue, albumIdValue));
      }
    });
  }

  public void init(String instanceId, String albumId) {
    this.instanceIdValue = instanceId;
    this.albumIdValue = albumId;
    this.componentId.setValue(instanceId);
    this.albumId.setValue(albumId);
  }


  @UiHandler("file")
  void upload(ChangeEvent event) {
    upload.submit();
    Notification.activityStart();
    upload.reset();
  }

  @UiHandler("link")
  void upload(ClickEvent event) {
    clickOnInputFile(file.getElement());
  }

  private static native void clickOnInputFile(Element elem) /*-{
    elem.click();
  }-*/;

}