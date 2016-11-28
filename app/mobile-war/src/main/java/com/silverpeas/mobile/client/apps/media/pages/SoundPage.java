package com.silverpeas.mobile.client.apps.media.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
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
import com.silverpeas.mobile.client.common.navigation.UrlUtils;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndEvent;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndHandler;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEvent;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeRecognizer;
import com.silverpeas.mobile.client.components.base.ActionsMenu;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.resources.ApplicationResources;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;
import com.silverpeas.mobile.shared.dto.media.SoundDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

/**
 * @author: svu
 */
public class SoundPage extends PageContent implements View, MediaPagesEventHandler,
    SwipeEndHandler {

  interface SoundPageUiBinder extends UiBinder<HTMLPanel, SoundPage> {
  }

  @UiField HeadingElement mediaTitle;
  @UiField Anchor mediaFullSize, download;
  @UiField ParagraphElement lastUpdate;
  @UiField SpanElement mediaFileName, weight, dimensions;
  @UiField ImageElement mediaPreview, mediaType;
  @UiField AudioElement player;
  @UiField CommentsButton comments;
  @UiField DivElement previewContainer;
  @UiField ActionsMenu actionsMenu;

  private NotifyButton notification = new NotifyButton();
  private static SoundPageUiBinder uiBinder = GWT.create(SoundPageUiBinder.class);
  private ApplicationResources resources = GWT.create(ApplicationResources.class);
  private SoundDTO sound;
  private MediaMessages msg;
  private SwipeRecognizer swipeRecognizer;

  public SoundPage() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(MediaMessages.class);
    getElement().setId("a-media");
    EventBus.getInstance().addHandler(AbstractMediaPagesEvent.TYPE, this);
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
      mediaPreview.setSrc(resources.sound().getSafeUri().asString());
      SoundDTO sound = (SoundDTO) event.getPreview();
      this.sound = sound;
      String url = UrlUtils.getServicesLocation();
      url += "SoundAction";
      url = url + "?id=" + sound.getId();
      player.setSrc(url);
      player.setAutoplay(false);
      player.setControls(true);
      player.setPreload("none");
      player.setAttribute("type", sound.getMimeType());

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
      if (event.isNotifiable()) {
        notification.init(sound.getInstance(), sound.getId(), NotificationDTO.TYPE_SOUND, sound.getName(), getPageTitle());
        actionsMenu.addAction(notification);
      }
    }
  }

  @Override
  public void onSwipeEnd(final SwipeEndEvent event) {
    if (isVisible()) {
      if (event.getDirection() == SwipeEvent.DIRECTION.RIGHT_TO_LEFT) {
        EventBus.getInstance().fireEvent(new MediaViewGetNextEvent(sound));
      } else if (event.getDirection() == SwipeEvent.DIRECTION.LEFT_TO_RIGHT) {
        EventBus.getInstance().fireEvent(new MediaViewGetPreviousEvent(sound));
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