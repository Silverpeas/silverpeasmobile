/*
 * Copyright (C) 2000 - 2021 Silverpeas
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
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndEvent;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndHandler;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEvent;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeRecognizer;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.comments.CommentDTO;
import org.silverpeas.mobile.shared.dto.media.VideoStreamingDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

/**
 * @author: svu
 */
public class VideoStreamingPage extends PageContent
    implements View, MediaPagesEventHandler, SwipeEndHandler {

  interface VideoStreamingPageUiBinder extends UiBinder<HTMLPanel, VideoStreamingPage> {
  }

  @UiField HeadingElement mediaTitle;
  @UiField Anchor mediaFullSize, download;
  @UiField ParagraphElement lastUpdate, creator;
  @UiField SpanElement mediaFileName, weight, dimensions;
  @UiField ImageElement mediaType;
  @UiField
  CommentsButton comments;
  @UiField IFrameElement player;
  @UiField DivElement previewContainer;
  @UiField
  ActionsMenu actionsMenu;

  private NotifyButton notification = new NotifyButton();
  private AddToFavoritesButton favorite = new AddToFavoritesButton();
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

      creator.setInnerHTML(msg.creation(video.getCreationDate(), video.getCreator()));
      lastUpdate.setInnerHTML(msg.lastUpdate(video.getUpdateDate(), video.getUpdater()));

      if (event.isCommentable()) {
        comments.init(video.getId(), video.getInstance(), CommentDTO.TYPE_STREAMING, getPageTitle(),
            video.getTitle(), video.getCommentsNumber());
      } else {
        comments.getElement().getStyle().setDisplay(Style.Display.NONE);
      }
      favorite.init(video.getInstance(), video.getId(), ContentsTypes.Media.name(), video.getTitle());
      actionsMenu.addAction(favorite);
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