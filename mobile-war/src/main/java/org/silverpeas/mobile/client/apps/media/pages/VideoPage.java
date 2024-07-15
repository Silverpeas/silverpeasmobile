/*
 * Copyright (C) 2000 - 2024 Silverpeas
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
import com.google.gwt.dom.client.VideoElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
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
import org.silverpeas.mobile.client.common.Html5Utils;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.common.navigation.LinksManager;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.common.network.NetworkHelper;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndEvent;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndHandler;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEvent;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeRecognizer;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.base.widgets.ShareButton;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.comments.CommentDTO;
import org.silverpeas.mobile.shared.dto.media.VideoDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

/**
 * @author: svu
 */
public class VideoPage extends PageContent implements View, MediaPagesEventHandler,
    SwipeEndHandler {

  interface VideoPageUiBinder extends UiBinder<HTMLPanel, VideoPage> {
  }

  @UiField HeadingElement mediaTitle;
  @UiField Anchor mediaFullSize, download, link;
  @UiField ParagraphElement lastUpdate, creator;
  @UiField SpanElement mediaFileName, weight, dimensions;
  @UiField ImageElement mediaType;
  @UiField CommentsButton comments;
  @UiField VideoElement player;
  @UiField DivElement previewContainer;

  @UiField
  HTMLPanel operations;

  private NotifyButton notification = new NotifyButton();
  private ShareButton shareBtn = new ShareButton();
  private AddToFavoritesButton favorite = new AddToFavoritesButton();

  private static VideoPageUiBinder uiBinder = GWT.create(VideoPageUiBinder.class);
  private VideoDTO video;
  private MediaMessages msg;
  private ApplicationResources resources = GWT.create(ApplicationResources.class);
  private SwipeRecognizer swipeRecognizer;

  public VideoPage() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(MediaMessages.class);
    EventBus.getInstance().addHandler(AbstractMediaPagesEvent.TYPE, this);
    getElement().setId("a-media");

    operations.getElement().setId("operations");
    download.getElement().setId("download");

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
      this.video = (VideoDTO) event.getPreview();
      if (!video.getDownload()) link.getElement().removeClassName("expand-more");
      String url = UrlUtils.getSilverpeasServicesLocation();
      url += "gallery/" + video.getInstance() + "/videos/" + video.getId() + "/content";
      player.setSrc(url);
      player.setAutoplay(false);
      player.setControls(true);
      player.setPreload("none");
      player.setAttribute("controlsList", "nodownload");
      player.setPoster(video.getDataPoster());
      player.setHeight(200);
      player.setAttribute("style", "max-width:" + Window.getClientWidth() * 0.8 + "px");
      player.setAttribute("type", video.getMimeType());
      mediaFullSize.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(final ClickEvent clickEvent) {
          if (player.isPaused()) {
            player.play();
            Html5Utils.setVideoFullScreen(player);
          } else {
            player.pause();
            Html5Utils.setVideoNotFullScreen(player);
          }
        }
      });
      mediaType.setSrc(NetworkHelper.getContext() + "icons/files/file-type-video.svg");
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

      creator.setInnerHTML(msg.creation(video.getCreationDate(), video.getCreator()));
      lastUpdate.setInnerHTML(msg.lastUpdate(video.getUpdateDate(), video.getUpdater()));

      if (event.isCommentable()) {
        comments.init(video.getId(), CommentDTO.TYPE_VIDEO, getPageTitle(),
            video.getTitle(), video.getCommentsNumber(), getApp().getApplicationInstance());
      } else {
        comments.getElement().getStyle().setDisplay(Style.Display.NONE);
      }
      favorite.init(video.getInstance(), video.getId(), ContentsTypes.Media.name(), video.getTitle());
      addActionMenu(favorite);
      if (event.isNotifiable()) {
        notification.init(video.getInstance(), video.getId(), NotificationDTO.TYPE_VIDEO, video.getName(), getPageTitle());
        addActionMenu(notification);
      }
      shareBtn.init(video.getTitle(),video.getTitle(), LinksManager.createMediaPermalink(video.getId()));
      addActionMenu(shareBtn);
    }
  }

  @UiHandler("link")
  void actions(ClickEvent event) {
    if (video.getDownload()) {
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

  @UiHandler("download")
  void download(ClickEvent event) {
    if (video.getDownload()) {
      if (!clicked) {
        clicked = true;
        try {
          String url = UrlUtils.getSilverpeasServicesLocation();
          url += "gallery/" + video.getInstance() + "/videos/" + video.getId() + "/content";
          download.setHref(url);
          download.setTarget("_self");
          download.getElement().setAttribute("download", video.getTitle());
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