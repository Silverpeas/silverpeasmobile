package com.silverpeas.mobile.client.apps.media.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.comments.pages.widgets.CommentsButton;
import com.silverpeas.mobile.client.apps.media.events.pages.AbstractMediaPagesEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaPagesEventHandler;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaPreviewLoadedEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaViewLoadedEvent;
import com.silverpeas.mobile.client.apps.media.resources.MediaMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.resources.ApplicationResources;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;
import com.silverpeas.mobile.shared.dto.media.SoundDTO;

/**
 * @author: svu
 */
public class SoundPage extends PageContent implements View, MediaPagesEventHandler {

  interface SoundPageUiBinder extends UiBinder<HTMLPanel, SoundPage> {
  }

  @UiField HeadingElement mediaTitle;
  @UiField Anchor mediaFullSize, download;
  @UiField ParagraphElement lastUpdate;
  @UiField SpanElement mediaFileName, weight, dimensions;
  @UiField ImageElement mediaPreview, mediaType;
  @UiField AudioElement player;
  @UiField CommentsButton comments;
  private static SoundPageUiBinder uiBinder = GWT.create(SoundPageUiBinder.class);
  private ApplicationResources resources = GWT.create(ApplicationResources.class);
  private SoundDTO sound;
  private MediaMessages msg;

  public SoundPage() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(MediaMessages.class);
    getElement().setId("a-media");
    EventBus.getInstance().addHandler(AbstractMediaPagesEvent.TYPE, this);
  }

  @Override
  public void onMediaPreviewLoaded(final MediaPreviewLoadedEvent event) {
    if (isVisible()) {
      mediaPreview.setSrc(resources.sound().getSafeUri().asString());
      SoundDTO sound = (SoundDTO) event.getPreview();
      this.sound = sound;
      String url = Window.Location.getProtocol() + "//" + Window.Location.getHost() + Window.Location.getPath() + "spmobil/SoundAction";
      url = url + "?id=" + sound.getId() + "&instanceId=" + sound.getInstance() + "&userId=" + SpMobil.user.getId();
      player.setSrc(url);
      player.setAutoplay(true);
      player.setControls(true);

      Image img = new Image(resources.sound());
      mediaType.getParentElement().replaceChild(img.getElement(), mediaType);
      mediaTitle.setInnerHTML(sound.getTitle());
      mediaFileName.setInnerHTML(sound.getName());

      String size;
      if (sound.getSize() < 1024 * 1024) {
        size = String.valueOf(sound.getSize() / 1024);
        weight.setInnerHTML(msg.sizeK(size));
      } else {
        size = String.valueOf(sound.getSize() / (1024 * 1024));
        weight.setInnerHTML(msg.sizeM(size));
      }
      dimensions.setInnerHTML(String.valueOf(sound.getDuration()));
      lastUpdate.setInnerHTML(msg.lastUpdate(sound.getUpdateDate(), sound.getUpdater()));

      if (sound.isDownload()) {
        download.setHref(url);
        download.setTarget("_self");
      }

      if (event.isCommentable()) {
        comments.init(sound.getId(), sound.getInstance(), CommentDTO.TYPE_SOUND, getPageTitle(),
            sound.getTitle(), sound.getCommentsNumber());
      } else {
        comments.getElement().getStyle().setDisplay(Style.Display.NONE);
      }
    }
  }

  @Override
  public void onMediaViewLoaded(final MediaViewLoadedEvent event) {
  }

  @Override
  public void stop() {
    super.stop();
    comments.stop();
    EventBus.getInstance().removeHandler(AbstractMediaPagesEvent.TYPE, this);
  }
}