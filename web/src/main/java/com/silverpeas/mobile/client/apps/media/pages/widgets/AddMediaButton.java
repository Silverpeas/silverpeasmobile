package com.silverpeas.mobile.client.apps.media.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.media.resources.MediaMessages;




/**
 * @author: svu
 */
public class AddMediaButton extends Composite {
  interface AddMediaButtonUiBinder extends UiBinder<Widget, AddMediaButton> {
  }

  @UiField FileUpload file;
  @UiField FormPanel upload;
  @UiField Hidden componentId, albumId;
  @UiField(provided = true) protected MediaMessages msg = null;

  private static AddMediaButtonUiBinder uiBinder = GWT.create(AddMediaButtonUiBinder.class);

  public AddMediaButton() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(MediaMessages.class);

    file.getElement().setAttribute("accept", "image/*");
    file.getElement().setAttribute("multiple", "multiple");
    upload.setEncoding(FormPanel.ENCODING_MULTIPART);
  }

  public void init(String instanceId, String albumId) {
    this.componentId.setValue(instanceId);
    this.albumId.setValue(albumId);
  }

  @UiHandler("file")
  void upload(ChangeEvent event) {
    upload.submit();
    upload.reset();
    //TODO : reload media list
  }

}