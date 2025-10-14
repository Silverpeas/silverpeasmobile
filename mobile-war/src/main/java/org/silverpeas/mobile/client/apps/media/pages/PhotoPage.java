/*
 * Copyright (C) 2000 - 2025 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.apps.media.pages;

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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.silverpeas.mobile.client.apps.comments.pages.widgets.CommentsButton;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.media.events.app.MediaViewGetNextEvent;
import org.silverpeas.mobile.client.apps.media.events.app.MediaViewGetPreviousEvent;
import org.silverpeas.mobile.client.apps.media.events.app.MediaViewShowEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.AbstractMediaPagesEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaPagesEventHandler;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaPreviewLoadedEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaViewNextEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaViewPrevEvent;
import org.silverpeas.mobile.client.apps.media.resources.MediaMessages;
import org.silverpeas.mobile.client.apps.notifications.pages.widgets.NotifyButton;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.common.network.NetworkHelper;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndEvent;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndHandler;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEvent;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeRecognizer;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.base.widgets.ShareButton;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.comments.CommentDTO;
import org.silverpeas.mobile.shared.dto.media.PhotoDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

/**
 * @author: svu
 */
public class PhotoPage extends PageContent implements View, MediaPagesEventHandler, SwipeEndHandler {

  interface PhotoPageUiBinder extends UiBinder<HTMLPanel, PhotoPage> {
  }

  @UiField HeadingElement mediaTitle;
  @UiField Anchor mediaFullSize, download, link;
  @UiField ParagraphElement lastUpdate, creator;
  @UiField SpanElement mediaFileName, weight, dimensions;
  @UiField ImageElement preview, mediaType;
  @UiField
  CommentsButton comments;
  @UiField DivElement previewContainer;

  @UiField HTML view;

  @UiField HTMLPanel operations;

  private NotifyButton notification = new NotifyButton();
  private AddToFavoritesButton favorite = new AddToFavoritesButton();

  private ShareButton share = new ShareButton();

  private static PhotoPageUiBinder uiBinder = GWT.create(PhotoPageUiBinder.class);
  private PhotoDTO photo;
  private MediaMessages msg;
  private SwipeRecognizer swipeRecognizer;

  public PhotoPage() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(MediaMessages.class);
    EventBus.getInstance().addHandler(AbstractMediaPagesEvent.TYPE, this);
    getElement().setId("a-media");
    operations.getElement().setId("operations");
    download.getElement().setId("download");
    view.getElement().setId("view");
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
      if (!photo.getDownload()) link.getElement().removeClassName("expand-more");
      preview.setSrc(photo.getDataPhoto());
      mediaType.setSrc(NetworkHelper.getContext() + "icons/files/file-type-image.svg");
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

      creator.setInnerHTML(msg.creation(photo.getCreationDate(), photo.getCreator()));
      lastUpdate.setInnerHTML(msg.lastUpdate(photo.getUpdateDate(), photo.getUpdater()));

      if (event.isCommentable()) {
        comments.init(photo.getId(), CommentDTO.TYPE_PHOTO, getPageTitle(),
            photo.getTitle(), photo.getCommentsNumber(), getApp().getApplicationInstance());
      } else {
        comments.getElement().getStyle().setDisplay(Style.Display.NONE);
      }

      favorite.init(photo.getInstance(), photo.getId(), ContentsTypes.Media.name(), photo.getTitle());
      addActionMenu(favorite);
      if (event.isNotifiable()) {
        notification.init(photo.getInstance(), photo.getId(), NotificationDTO.TYPE_PHOTO, photo.getName(), getPageTitle());
        addActionMenu(notification);
      }
      share.init(photo.getTitle(),photo.getTitle(),"/silverpeas/Media/"+photo.getId());
      addActionMenu(share);
    }
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
    PhotoViewerPage page = new PhotoViewerPage();
    page.setDataPhoto(photo);
    page.show();
  }

  @UiHandler("link")
  void link(ClickEvent event) {
    if (photo.getDownload()) {
      toogleOperations();
    }
  }

  private void toogleOperations() {
    if (operations.getStylePrimaryName().equalsIgnoreCase("ops-closed")) {
      operations.setStylePrimaryName("ops-open");
      link.setStylePrimaryName("expand-less");
    } else {
      operations.setStylePrimaryName("ops-closed");
      link.setStylePrimaryName("expand-more");
    }
  }

  @UiHandler("view")
  void view(ClickEvent event) {
    showFullScreen(event);
  }

  @UiHandler("download")
  void download(ClickEvent event) {
    if (photo.getDownload()) {
      if (!clicked) {
        clicked = true;
        try {
          String url = UrlUtils.getUploadLocation();
          url += "MediaAction";
          url += "?action=view" + "&id=" + photo.getId() + "&instanceId=" + photo.getInstance();
          download.setHref(url);
          download.setTarget("_self");
          download.getElement().setAttribute("download", photo.getTitle());
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