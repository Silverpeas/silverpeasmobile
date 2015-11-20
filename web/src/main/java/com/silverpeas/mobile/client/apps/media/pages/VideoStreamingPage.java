package com.silverpeas.mobile.client.apps.media.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.silverpeas.mobile.client.apps.comments.pages.widgets.CommentsButton;
import com.silverpeas.mobile.client.apps.media.events.app.MediaViewGetNextEvent;
import com.silverpeas.mobile.client.apps.media.events.app.MediaViewGetPreviousEvent;
import com.silverpeas.mobile.client.apps.media.events.app.MediaViewShowEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.AbstractMediaPagesEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaPagesEventHandler;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaPreviewLoadedEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaViewLoadedEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaViewNextEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaViewPrevEvent;
import com.silverpeas.mobile.client.apps.media.resources.MediaMessages;
import com.silverpeas.mobile.client.apps.notifications.pages.widgets.NotifyButton;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndEvent;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndHandler;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEvent;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeRecognizer;
import com.silverpeas.mobile.client.components.base.ActionsMenu;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.resources.ApplicationResources;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;
import com.silverpeas.mobile.shared.dto.media.VideoStreamingDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

/**
 * @author: svu
 */
public class VideoStreamingPage extends PageContent implements View, MediaPagesEventHandler,
    SwipeEndHandler {

  interface VideoStreamingPageUiBinder extends UiBinder<HTMLPanel, VideoStreamingPage> {
  }

  @UiField HeadingElement mediaTitle;
  @UiField Anchor mediaFullSize, download;
  @UiField ParagraphElement lastUpdate;
  @UiField SpanElement mediaFileName, weight, dimensions;
  @UiField ImageElement mediaType;
  @UiField CommentsButton comments;
  @UiField IFrameElement player;
  @UiField DivElement previewContainer;
  @UiField ActionsMenu actionsMenu;

  private NotifyButton notification = new NotifyButton();
  private static VideoStreamingPageUiBinder uiBinder = GWT.create(VideoStreamingPageUiBinder.class);
  private VideoStreamingDTO video;
  private MediaMessages msg;
  private ApplicationResources resources = GWT.create(ApplicationResources.class);
  private SwipeRecognizer swipeRecognizer;

  public VideoStreamingPage() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(MediaMessages.class);
    EventBus.getInstance().addHandler(AbstractMediaPagesEvent.TYPE, this);
    getElement().setId("a-media");
    /*Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        swipeRecognizer = new SwipeRecognizer(HTML.wrap(previewContainer));
      }
    });
    EventBus.getInstance().addHandler(SwipeEndEvent.getType(), this);*/
  }

  @Override
  public void onMediaPreviewLoaded(final MediaPreviewLoadedEvent event) {
    if (isVisible()) {
      this.video = (VideoStreamingDTO) event.getPreview();

      player.setSrc(video.getUrl());
      player.setFrameBorder(0);
      player.setAttribute("allowfullscreen", "");

      Image img = new Image(resources.streaming());
      mediaType.getParentElement().replaceChild(img.getElement(), mediaType);
      mediaTitle.setInnerHTML(video.getTitle());
      mediaFileName.setInnerHTML(video.getName());

      lastUpdate.setInnerHTML(msg.lastUpdate(video.getUpdateDate(), video.getUpdater()));

      if (event.isCommentable()) {
        comments.init(video.getId(), video.getInstance(), CommentDTO.TYPE_STREAMING, getPageTitle(),
            video.getTitle(), video.getCommentsNumber());
      } else {
        comments.getElement().getStyle().setDisplay(Style.Display.NONE);
      }
      if (event.isNotifiable()) {
        notification.init(video.getInstance(), video.getId(), NotificationDTO.TYPE_STREAMING, video.getName(), getPageTitle());
        actionsMenu.addAction(notification);
      }
    }
  }

  @Override
  public void onSwipeEnd(final SwipeEndEvent event) {
    if (isVisible()) {
      if (event.getDirection() == SwipeEvent.DIRECTION.RIGHT_TO_LEFT) {
        EventBus.getInstance().fireEvent(new MediaViewGetNextEvent(video));
      } else if (event.getDirection() == SwipeEvent.DIRECTION.LEFT_TO_RIGHT) {
        EventBus.getInstance().fireEvent(new MediaViewGetPreviousEvent(video));
      }
    }
  }

  @Override
  public void onMediaViewLoaded(final MediaViewLoadedEvent event) {
  }

  @Override
  public void onMediaViewNext(final MediaViewNextEvent mediaViewNextEvent) {
    EventBus.getInstance().fireEvent(new MediaViewShowEvent(mediaViewNextEvent.getNextMedia()));
    back();
  }

  @Override
  public void onMediaViewPrev(final MediaViewPrevEvent mediaViewPrevEvent) {
    EventBus.getInstance().fireEvent(new MediaViewShowEvent(mediaViewPrevEvent.getPrevMedia()));
    back();
  }

  @Override
  public void stop() {
    super.stop();
    comments.stop();
    EventBus.getInstance().removeHandler(AbstractMediaPagesEvent.TYPE, this);
    //EventBus.getInstance().removeHandler(SwipeEndEvent.getType(), this);
  }
}