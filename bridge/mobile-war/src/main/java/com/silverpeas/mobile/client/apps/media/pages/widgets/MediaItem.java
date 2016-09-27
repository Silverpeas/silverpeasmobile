package com.silverpeas.mobile.client.apps.media.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.silverpeas.mobile.client.apps.media.events.pages.navigation.MediaItemClickEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;
import com.silverpeas.mobile.shared.dto.media.MediaDTO;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;
import com.silverpeas.mobile.shared.dto.media.SoundDTO;
import com.silverpeas.mobile.shared.dto.media.VideoDTO;
import com.silverpeas.mobile.shared.dto.media.VideoStreamingDTO;

public class MediaItem extends Composite {

  private MediaDTO data;
  private static MediaItemUiBinder uiBinder = GWT.create(MediaItemUiBinder.class);
  @UiField Anchor link;
  @UiField ImageElement thumb;
  @UiField SpanElement detail;
  @UiField HTMLPanel container;
  private ApplicationResources resources = GWT.create(ApplicationResources.class);
  protected ApplicationMessages msg = null;

  interface MediaItemUiBinder extends UiBinder<Widget, MediaItem> {
  }

  public MediaItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void setData(MediaDTO data) {
    this.data = data;
    link.setTitle(data.getTitle());
    thumb.setAlt(data.getTitle());
    detail.setInnerText(data.getTitle());

    if (data instanceof PhotoDTO) {
      thumb.setSrc( ((PhotoDTO)data).getDataPhoto());
    } else if (data instanceof SoundDTO) {
      thumb.setSrc(resources.sound().getSafeUri().asString());
    } else if (data instanceof VideoDTO) {
      thumb.setSrc(((VideoDTO) data).getDataPoster());
    } else if (data instanceof VideoStreamingDTO) {
      String url = ((VideoStreamingDTO) data).getUrlPoster();
      if (url.isEmpty()) {
        thumb.setSrc(resources.streaming().getSafeUri().asString());
      } else {
        thumb.setSrc(url);
      }
    }
  }

  @UiHandler("link")
  protected void onClick(ClickEvent event) {
    EventBus.getInstance().fireEvent(new MediaItemClickEvent(data));
  }
}
