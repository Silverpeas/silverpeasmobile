package com.silverpeas.mobile.client.apps.media.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SourceElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.media.client.Audio;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
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
import com.silverpeas.mobile.shared.dto.media.SoundDTO;

/**
 * @author: svu
 */
public class SoundPage extends PageContent implements View, MediaPagesEventHandler {

  interface MediaPageUiBinder extends UiBinder<HTMLPanel, SoundPage> {
  }

  @UiField HeadingElement mediaTitle;
  @UiField Anchor mediaFullSize, download;
  @UiField ParagraphElement lastUpdate;
  @UiField SpanElement mediaFileName, weight, dimensions;
  @UiField ImageElement mediaPreview, mediaType;
  @UiField AudioElement player;
  @UiField CommentsButton comments;
  private static MediaPageUiBinder uiBinder = GWT.create(MediaPageUiBinder.class);
  private ApplicationResources resources = GWT.create(ApplicationResources.class);
  private SoundDTO sound;
  private MediaMessages msg;

  public SoundPage() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(MediaMessages.class);
    EventBus.getInstance().addHandler(AbstractMediaPagesEvent.TYPE, this);
    getElement().setId("a-media");
    mediaPreview.setSrc(resources.sound().getSafeUri().asString());
  }

  public void setSound(SoundDTO sound) {
    this.sound = sound;
    String url = Window.Location.getProtocol() + "//" + Window.Location.getHost() + Window.Location.getPath() + "spmobil/SoundAction";
    url = url + "?id=" + sound.getId() + "&instanceId=" + sound.getInstance() + "&userId=" + SpMobil.user.getId();
    player.setSrc(url);
    player.setAutoplay(true);
    player.setControls(true);
  }

  @Override
  public void onMediaPreviewLoaded(final MediaPreviewLoadedEvent event) {
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

  @UiHandler("download")
  void download(ClickEvent event) {
    if (sound.isDownload()) {
      if (!clicked) {
        clicked = true;
        //TODO : enable download file
      }
    }
  }
}