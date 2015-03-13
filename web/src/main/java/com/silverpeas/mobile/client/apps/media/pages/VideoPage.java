package com.silverpeas.mobile.client.apps.media.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.VideoElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.comments.pages.widgets.CommentsButton;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import com.silverpeas.mobile.client.apps.media.events.app.MediaViewLoadEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.AbstractMediaPagesEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaPagesEventHandler;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaPreviewLoadedEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaViewLoadedEvent;
import com.silverpeas.mobile.client.apps.media.resources.MediaMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.navigation.UrlUtils;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.resources.ApplicationResources;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;
import com.silverpeas.mobile.shared.dto.media.SoundDTO;
import com.silverpeas.mobile.shared.dto.media.VideoDTO;

/**
 * @author: svu
 */
public class VideoPage extends PageContent implements View, MediaPagesEventHandler {

  interface VideoPageUiBinder extends UiBinder<HTMLPanel, VideoPage> {
  }

  @UiField HeadingElement mediaTitle;
  @UiField Anchor mediaFullSize, download;
  @UiField ParagraphElement lastUpdate;
  @UiField SpanElement mediaFileName, weight, dimensions;
  @UiField ImageElement mediaType;
  @UiField CommentsButton comments;
  @UiField VideoElement player;
  private static VideoPageUiBinder uiBinder = GWT.create(VideoPageUiBinder.class);
  private VideoDTO video;
  private MediaMessages msg;
  private ApplicationResources resources = GWT.create(ApplicationResources.class);

  public VideoPage() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(MediaMessages.class);
    EventBus.getInstance().addHandler(AbstractMediaPagesEvent.TYPE, this);
    getElement().setId("a-media");
  }

  @Override
  public void onMediaPreviewLoaded(final MediaPreviewLoadedEvent event) {
    if (isVisible()) {
      this.video = (VideoDTO) event.getPreview();
      String url = UrlUtils.getLocation();
      url += "spmobil/VideoAction?id=" + video.getId();
      player.setSrc(url);
      player.setAutoplay(false);
      player.setControls(true);
      player.setPreload("none");
      player.setPoster(video.getDataPoster());
      player.setHeight(140);
      player.setAttribute("type", video.getMimeType());

      Image img = new Image(resources.video());
      mediaType.getParentElement().replaceChild(img.getElement(), mediaType);
      mediaTitle.setInnerHTML(video.getTitle());
      mediaFileName.setInnerHTML(video.getName());

      String size;
      if (video.getSize() < 1024 * 1024) {
        size = String.valueOf(video.getSize() / 1024);
        weight.setInnerHTML(msg.sizeK(size));
      } else {
        size = String.valueOf(video.getSize() / (1024 * 1024));
        weight.setInnerHTML(msg.sizeM(size));
      }

      dimensions.setInnerHTML(String.valueOf(video.getDuration()));

      lastUpdate.setInnerHTML(msg.lastUpdate(video.getUpdateDate(), video.getUpdater()));

      if (event.isCommentable()) {
        comments.init(video.getId(), video.getInstance(), CommentDTO.TYPE_PHOTO, getPageTitle(),
            video.getTitle(), video.getCommentsNumber());
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