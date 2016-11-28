package com.silverpeas.mobile.client.apps.media.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.silverpeas.mobile.client.apps.comments.pages.widgets.CommentsButton;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import com.silverpeas.mobile.client.apps.media.events.app.MediaViewGetNextEvent;
import com.silverpeas.mobile.client.apps.media.events.app.MediaViewGetPreviousEvent;
import com.silverpeas.mobile.client.apps.media.events.app.MediaViewLoadEvent;
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
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.navigation.UrlUtils;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndEvent;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndHandler;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEvent;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeRecognizer;
import com.silverpeas.mobile.client.components.base.ActionsMenu;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

/**
 * @author: svu
 */
public class PhotoPage extends PageContent implements View, MediaPagesEventHandler, SwipeEndHandler {

  interface PhotoPageUiBinder extends UiBinder<HTMLPanel, PhotoPage> {
  }

  @UiField HeadingElement mediaTitle;
  @UiField Anchor mediaFullSize, download;
  @UiField ParagraphElement lastUpdate;
  @UiField SpanElement mediaFileName, weight, dimensions;
  @UiField ImageElement preview, mediaType;
  @UiField CommentsButton comments;
  @UiField DivElement previewContainer;

  @UiField ActionsMenu actionsMenu;

  private NotifyButton notification = new NotifyButton();

  private static PhotoPageUiBinder uiBinder = GWT.create(PhotoPageUiBinder.class);
  private PhotoDTO photo;
  private DocumentsResources ressources;
  private MediaMessages msg;
  private SwipeRecognizer swipeRecognizer;

  public PhotoPage() {
    initWidget(uiBinder.createAndBindUi(this));
    ressources = GWT.create(DocumentsResources.class);
    ressources.css().ensureInjected();
    msg = GWT.create(MediaMessages.class);
    EventBus.getInstance().addHandler(AbstractMediaPagesEvent.TYPE, this);
    getElement().setId("a-media");
    /*Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        swipeRecognizer = new SwipeRecognizer(HTML.wrap(previewContainer));
      }
    });*/
    //EventBus.getInstance().addHandler(SwipeEndEvent.getType(), this);
  }

  @Override
  public void onSwipeEnd(final SwipeEndEvent event) {
    if (isVisible()) {
      if (event.getDirection() == SwipeEvent.DIRECTION.RIGHT_TO_LEFT) {
        EventBus.getInstance().fireEvent(new MediaViewGetNextEvent(photo));
      } else if (event.getDirection() == SwipeEvent.DIRECTION.LEFT_TO_RIGHT) {
        EventBus.getInstance().fireEvent(new MediaViewGetPreviousEvent(photo));
      }
    }
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
  public void onMediaPreviewLoaded(final MediaPreviewLoadedEvent event) {
    if (isVisible()) {
      this.photo = (PhotoDTO) event.getPreview();
      preview.setSrc(photo.getDataPhoto());
      Image img = new Image(ressources.image());
      mediaType.getParentElement().replaceChild(img.getElement(), mediaType);
      mediaTitle.setInnerHTML(photo.getTitle());
      mediaFileName.setInnerHTML(photo.getName());

      String size;
      if (photo.getSize() < 1024 * 1024) {
        size = String.valueOf(photo.getSize() / 1024);
        weight.setInnerHTML(msg.sizeK(size));
      } else {
        size = String.valueOf(photo.getSize() / (1024 * 1024));
        weight.setInnerHTML(msg.sizeM(size));
      }

      dimensions.setInnerHTML(msg.dimensions(String.valueOf(photo.getSizeL()), String.valueOf(photo.getSizeH())));

      lastUpdate.setInnerHTML(msg.lastUpdate(photo.getUpdateDate(), photo.getUpdater()));

      if (event.isCommentable()) {
        comments.init(photo.getId(), photo.getInstance(), CommentDTO.TYPE_PHOTO, getPageTitle(),
            photo.getTitle(), photo.getCommentsNumber());
      } else {
        comments.getElement().getStyle().setDisplay(Style.Display.NONE);
      }
      if (event.isNotifiable()) {
        notification.init(photo.getInstance(), photo.getId(), NotificationDTO.TYPE_PHOTO, photo.getName(), getPageTitle());
        actionsMenu.addAction(notification);
      }
    }
  }

  @Override
  public void onMediaViewLoaded(final MediaViewLoadedEvent event) {
    Notification.activityStop();

    PhotoViewerPage page = new PhotoViewerPage();
    page.setDataPhoto(event.getView().getDataPhoto());
    page.show();
  }

  @Override
  public void stop() {
    super.stop();
    comments.stop();
    EventBus.getInstance().removeHandler(AbstractMediaPagesEvent.TYPE, this);
    //EventBus.getInstance().removeHandler(SwipeEndEvent.getType(), this);
  }

  @UiHandler("mediaFullSize")
  void showFullScreen(ClickEvent event) {
    Notification.activityStart();
    EventBus.getInstance().fireEvent(new MediaViewLoadEvent(photo.getInstance(), photo.getId()));
  }

  @UiHandler("download")
  void download(ClickEvent event) {
    if (photo.isDownload()) {
      if (!clicked) {
        clicked = true;
        try {
          String url = UrlUtils.getServicesLocation();
          url += "MediaAction";
          url += "?action=view" + "&id=" + photo.getId() + "&instanceId=" + photo.getInstance();
          download.setHref(url);
          download.setTarget("_self");
          download.fireEvent(new ClickEvent() {});

        } catch(JavaScriptException e) {
          Notification.alert(e.getMessage());
        }

        Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
          @Override
          public boolean execute() {
            clicked = false;
            return false;
          }}, 400);
      }
    }
  }
}