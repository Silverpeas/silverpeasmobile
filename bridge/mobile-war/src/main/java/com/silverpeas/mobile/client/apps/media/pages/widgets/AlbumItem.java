package com.silverpeas.mobile.client.apps.media.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.media.events.pages.navigation.MediaItemClickEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.media.AlbumDTO;

public class AlbumItem extends Composite {

  private AlbumDTO data;
  private static MediaItemUiBinder uiBinder = GWT.create(MediaItemUiBinder.class);
  @UiField Anchor link;
  protected ApplicationMessages msg = null;


  interface MediaItemUiBinder extends UiBinder<Widget, AlbumItem> {
  }

  public AlbumItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void setData(AlbumDTO data) {
    this.data = data;
    link.setText(data.getName() + " (" + data.getCountMedia() + ")");
  }

  @UiHandler("link")
  protected void onClick(ClickEvent event) {
    EventBus.getInstance().fireEvent(new MediaItemClickEvent(data));
  }
}
