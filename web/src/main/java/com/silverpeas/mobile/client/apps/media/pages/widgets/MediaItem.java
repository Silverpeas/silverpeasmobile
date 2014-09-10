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
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;

public class MediaItem extends Composite {

  private Object data;
  private AlbumDTO dataAlbum;
  private PhotoDTO dataPhoto;
  private static MediaItemUiBinder uiBinder = GWT.create(MediaItemUiBinder.class);
  @UiField Anchor link;
  protected ApplicationMessages msg = null;


  interface MediaItemUiBinder extends UiBinder<Widget, MediaItem> {
  }

  public MediaItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void setData(Object data) {
    this.data = data;
    if (data instanceof AlbumDTO) {
      dataAlbum = (AlbumDTO) data;
      link.setText(dataAlbum.getName() + " (" + dataAlbum.getCountMedia() + ")");
      setStyleName("folder-galery");
    } else if (data instanceof PhotoDTO) {
      dataPhoto = (PhotoDTO) data;
      link.setText(dataPhoto.getTitle());
      setStyleName("media");
    }
    link.setStyleName("ui-btn ui-btn-icon-right ui-icon-carat-r");
  }

  @UiHandler("link")
  protected void onClick(ClickEvent event) {
    EventBus.getInstance().fireEvent(new MediaItemClickEvent(data));
  }
}
