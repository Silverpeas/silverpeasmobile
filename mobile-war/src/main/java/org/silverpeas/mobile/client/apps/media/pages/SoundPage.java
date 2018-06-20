/*
 * Copyright (C) 2000 - 2018 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
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
import com.google.gwt.dom.client.AudioElement;
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
import org.silverpeas.mobile.client.apps.media.events.pages.MediaViewLoadedEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaViewNextEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaViewPrevEvent;
import org.silverpeas.mobile.client.apps.media.resources.MediaMessages;
import org.silverpeas.mobile.client.apps.notifications.pages.widgets.NotifyButton;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndEvent;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndHandler;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEvent;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeRecognizer;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.comments.CommentDTO;
import org.silverpeas.mobile.shared.dto.media.SoundDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

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
  private AddToFavoritesButton favorite = new AddToFavoritesButton();
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
      String url = UrlUtils.getSilverpeasServicesLocation();
      url += "gallery/" + sound.getInstance() + "/sounds/" + sound.getId() + "/content";
      player.setSrc(url);
      player.setAutoplay(false);
      player.setControls(true);
      player.setPreload("none");
      player.setAttribute("controlsList", "nodownload");
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

      if (event.isCommentable()) {
        comments.init(sound.getId(), sound.getInstance(), CommentDTO.TYPE_SOUND, getPageTitle(),
            sound.getTitle(), sound.getCommentsNumber());
      } else {
        comments.getElement().getStyle().setDisplay(Style.Display.NONE);
      }
      favorite.init(sound.getInstance(), sound.getId(), ContentsTypes.Media.name(), sound.getTitle());
      actionsMenu.addAction(favorite);
      if (event.isNotifiable()) {
        notification.init(sound.getInstance(), sound.getId(), NotificationDTO.TYPE_SOUND, sound.getName(), getPageTitle());
        actionsMenu.addAction(notification);
      }
    }
  }

  @UiHandler("download")
  void download(ClickEvent event) {
    if (sound.isDownload()) {
      if (!clicked) {
        clicked = true;
        try {
          String url = UrlUtils.getSilverpeasServicesLocation();
          url += "gallery/" + sound.getInstance() + "/sounds/" + sound.getId() + "/content";
          download.setHref(url);
          download.setTarget("_self");
          download.getElement().setAttribute("download", sound.getTitle());
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