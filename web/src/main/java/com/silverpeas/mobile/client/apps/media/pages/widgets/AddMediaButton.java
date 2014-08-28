package com.silverpeas.mobile.client.apps.media.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.media.resources.MediaMessages;

/**
 * @author: svu
 */
public class AddMediaButton extends Composite {
  interface AddMediaButtonUiBinder extends UiBinder<Widget, AddMediaButton> {
  }

  @UiField Anchor link;
  
  @UiField(provided = true) protected MediaMessages msg = null;

  private static AddMediaButtonUiBinder uiBinder = GWT.create(AddMediaButtonUiBinder.class);

  public AddMediaButton() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(MediaMessages.class);
  }

  @UiHandler("link")
  protected void onClick(ClickEvent event) {
    //EventBus.getInstance().fireEvent();
    //TODO
  }
}