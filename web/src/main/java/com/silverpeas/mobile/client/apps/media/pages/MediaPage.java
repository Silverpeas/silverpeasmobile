package com.silverpeas.mobile.client.apps.media.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.comments.CommentsApp;
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
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;

/**
 * @author: svu
 */
public class MediaPage extends PageContent implements View, MediaPagesEventHandler {

  interface MediaPageUiBinder extends UiBinder<HTMLPanel, MediaPage> {
  }

  @UiField HeadingElement mediaTitle;
  @UiField Anchor mediaFullSize, comments, download;
  @UiField ParagraphElement lastUpdate;
  @UiField SpanElement mediaFileName, weight, dimensions;
  @UiField ImageElement preview, mediaType;
  private static MediaPageUiBinder uiBinder = GWT.create(MediaPageUiBinder.class);
  private PhotoDTO media;
  private MediaMessages msg;

  protected DocumentsResources ressources = null;

  public MediaPage() {
    initWidget(uiBinder.createAndBindUi(this));
    ressources = GWT.create(DocumentsResources.class);
    ressources.css().ensureInjected();
    msg = GWT.create(MediaMessages.class);
    EventBus.getInstance().addHandler(AbstractMediaPagesEvent.TYPE, this);
    getElement().setId("a-media");
  }

  @Override
  public void onMediaPreviewLoaded(final MediaPreviewLoadedEvent event) {
    this.media = event.getPreview();
    preview.setSrc(media.getDataPhoto());
    Image img = new Image(ressources.image());
    mediaType.getParentElement().replaceChild(img.getElement(), mediaType);
    mediaTitle.setInnerHTML(media.getTitle());
    mediaFileName.setInnerHTML(media.getName());

    String size;
    if (media.getSize() < 1024*1024) {
      size = String.valueOf(media.getSize()/1024);
      weight.setInnerHTML(msg.sizeK(size));
    } else {
      size = String.valueOf(media.getSize()/(1024*1024));
      weight.setInnerHTML(msg.sizeM(size));
    }

    dimensions.setInnerHTML(msg.dimensions(String.valueOf(media.getSizeL()), String.valueOf(media.getSizeH())));

    lastUpdate.setInnerHTML(msg.lastUpdate(media.getUpdateDate(), media.getUpdater()));

    if (media.getCommentsNumber() == 0) {
      comments.setText(msg.noComment());
    } else if (media.getCommentsNumber() == 1) {
      comments.setText(msg.comment());
    } else {
      comments.setText(msg.comments(String.valueOf(media.getCommentsNumber())));
    }
  }

  @Override
  public void onMediaViewLoaded(final MediaViewLoadedEvent event) {
    Image picture = new Image();
    picture.setUrl(event.getView().getDataPhoto());
    picture.getElement().getStyle().setWidth(100, Style.Unit.PCT); //TODO : do better for center view with best scale
    SpMobil.showFullScreen(picture, true);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractMediaPagesEvent.TYPE, this);
  }

  @UiHandler("mediaFullSize")
  void showFullScreen(ClickEvent event) {
    EventBus.getInstance().fireEvent(new MediaViewLoadEvent(media.getInstance(), media.getId()));
  }

  @UiHandler("download")
  void download(ClickEvent event) {
    if (media.isDownload()) {
      if (!clicked) {
        clicked = true;
        try {
          String url = Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/spmobile/spmobil/Media";
          url = url + "?id=" + media.getId() + "&instanceId=" + media.getInstance();
          download.setHref(url);
          download.setTarget("_self");
          download.fireEvent(new ClickEvent() {});

        } catch(JavaScriptException e) {
          Notification.alert(e.getMessage(), null, "error", "ok");
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

  @UiHandler("comments")
  void displayComments(ClickEvent event) {
    CommentsApp commentsApp = new CommentsApp(media.getId(), media.getTitle());
    commentsApp.start();
  }

}