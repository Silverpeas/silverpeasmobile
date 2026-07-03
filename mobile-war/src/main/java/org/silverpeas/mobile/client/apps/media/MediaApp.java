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

package org.silverpeas.mobile.client.apps.media;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import org.silverpeas.mobile.client.apps.media.events.app.*;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaPreviewLoadedEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaViewNextEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaViewPrevEvent;
import org.silverpeas.mobile.client.apps.media.pages.*;
import org.silverpeas.mobile.client.apps.media.resources.MediaMessages;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.network.NetworkHelper;
import org.silverpeas.mobile.client.common.network.rest.RestMethod;
import org.silverpeas.mobile.client.common.network.rest.RestMethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.components.base.events.page.DataLoadedEvent;
import org.silverpeas.mobile.client.components.base.events.page.LoadingDataFinishEvent;
import org.silverpeas.mobile.client.components.base.events.page.MoreDataLoadedEvent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.media.*;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;

import java.util.List;

public class MediaApp extends App implements NavigationEventHandler, MediaAppEventHandler {

  private MediaMessages msg;
  private ApplicationMessages globalMsg;

  // Model
  private boolean commentable, notifiable;
  private List<BaseDTO> currentAlbumsItems;

  public MediaApp() {
    super(Apps.gallery.name());
    msg = GWT.create(MediaMessages.class);
    globalMsg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(AbstractMediaAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  @Override
  public void start() {
    // no "super.start(lauchingPage);" this apps is used in another apps
  }

  @Override
  public void startWithContent(final ContentDTO content) {
    displayContent(content);
  }

  private void displayContent(ContentDTO contentSource) {

    RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<MediaDTO>() {

      @Override
      public void attempt() {
        ServicesLocator.getServiceMedia().getMedia(contentSource.getInstanceId(), contentSource.getId(), this);
      }

      @Override
      public void onSuccess(final RestMethod method, final MediaDTO media) {
        super.onSuccess(method, media);
        RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<ApplicationInstanceDTO>() {
          @Override
          public void attempt() {
            super.attempt();
            ServicesLocator.getServiceNavigation()
                    .getApp(media.getInstance(), media.getId(), contentSource.getType(), this);
          }
          @Override
          public void onFailure(final RestMethod method, final Throwable t) {
            super.onFailure(method, t);
            if (NetworkHelper.needToGoOffine(t)) {
              Notification.alert(globalMsg.needToBeOnline());
            } else {
              EventBus.getInstance().fireEvent(new ErrorEvent(t));
            }
          }

          @Override
          public void onSuccess(final RestMethod method,
                                final ApplicationInstanceDTO app) {
            super.onSuccess(method, app);
            commentable = app.getCommentable();
            notifiable = app.getNotifiable();
            displayContent(media);
          }
        };
        action.attempt();
      }
    };
    action.attempt();
  }

  private void displayContent(final MediaDTO item) {
    if (item instanceof PhotoDTO) {
      PhotoPage page = new PhotoPage();
      page.setPageTitle(msg.title());
      page.setApp(this);
      page.show();
      EventBus.getInstance().fireEvent(
          new MediaPreviewLoadEvent(item.getInstance(), ContentsTypes.Photo.toString(),
              item.getId(), null));
    } else if (item instanceof SoundDTO) {
      SoundPage page = new SoundPage();
      page.setPageTitle(msg.title());
      page.setApp(this);
      page.show();
      Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
        @Override
        public void execute() {
          EventBus.getInstance().fireEvent(
              new MediaPreviewLoadEvent(item.getInstance(), ContentsTypes.Sound.toString(),
                  item.getId(), item));
        }
      });
    } else if (item instanceof VideoDTO) {
      VideoPage page = new VideoPage();
      page.setPageTitle(msg.title());
      page.setApp(this);
      page.show();
      Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
        @Override
        public void execute() {
          EventBus.getInstance().fireEvent(
              new MediaPreviewLoadEvent(item.getInstance(), ContentsTypes.Video.toString(),
                  item.getId(), item));
        }
      });
    } else if (item instanceof VideoStreamingDTO) {
      VideoStreamingPage page = new VideoStreamingPage();
      page.setPageTitle(msg.title());
      page.setApp(this);
      page.show();
      Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
        @Override
        public void execute() {
          EventBus.getInstance().fireEvent(
              new MediaPreviewLoadEvent(item.getInstance(), ContentsTypes.Streaming.toString(),
                  item.getId(), item));
        }
      });
    }
  }

  @Override
  public void loadMediaShow(final MediaViewShowEvent mediaViewShowEvent) {
    displayContent(mediaViewShowEvent.getMedia());
  }

  @Override
  public void stop() {
    // never stop this app
  }

  @Override
  public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
    if (event.getInstance().getType().equals(Apps.gallery.name())) {
      this.commentable = event.getInstance().getCommentable();
      this.notifiable = event.getInstance().getNotifiable();
      setApplicationInstance(event.getInstance());
      MediaNavigationPage page = new MediaNavigationPage();
      page.setApp(this);
      page.setPageTitle(event.getInstance().getLabel());
      page.init(event.getInstance().getId(), null, event.getInstance().getRights());
      page.show();
    }
  }

  private void loadAppInstance(final ContentDTO content) {
    RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<ApplicationInstanceDTO>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceNavigation()
                .getApp(content.getInstanceId(), content.getId(), content.getType(), this);
      }
      @Override
      public void onSuccess(final RestMethod method,
                            final ApplicationInstanceDTO app) {
        super.onSuccess(method, app);
        setApplicationInstance(app);
        appInstanceChanged(new NavigationAppInstanceChangedEvent(app));
      }
    };
    action.attempt();
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getInstanceId() == null || !event.getContent().getInstanceId().startsWith(Apps.gallery.name())) return;
    if (event.getContent().getType().equals("Component")) {
      loadAppInstance(event.getContent());
    } else if (event.getContent().getType().equals(ContentsTypes.Media.name()) ||
        event.getContent().getType().equals(ContentsTypes.Photo.name()) ||
        event.getContent().getType().equals(ContentsTypes.Sound.name()) ||
        event.getContent().getType().equals(ContentsTypes.Video.name()) ||
        event.getContent().getType().equals(ContentsTypes.Streaming.name())) {
      startWithContent(event.getContent());
    } else if (event.getContent().getType().equals(ContentsTypes.Album.name()) ||
            event.getContent().getType().equals(ContentsTypes.Folder.name())) {
      RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<ApplicationInstanceDTO>() {
        @Override
        public void attempt() {
          super.attempt();
          ServicesLocator.getServiceNavigation().getApp(event.getContent().getInstanceId(), null, null, this);
        }

        @Override
        public void onFailure(final RestMethod method, final Throwable t) {
          super.onFailure(method, t);
          EventBus.getInstance().fireEvent(new ErrorEvent(t));
        }

        @Override
        public void onSuccess(final RestMethod method,
            final ApplicationInstanceDTO applicationInstanceDTO) {
          super.onSuccess(method, applicationInstanceDTO);
          MediaNavigationPage page = new MediaNavigationPage();
          page.setApp(MediaApp.this);
          page.init(event.getContent().getInstanceId(), event.getContent().getId(), applicationInstanceDTO.getRights());
          page.show();
        }
      };
      action.attempt();
    }
  }

  @Override
  public void loadAlbums(final MediasLoadMediaItemsEvent event) {
    boolean moreElements = true;
    int callNumber = 0;

    RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<StreamingList<BaseDTO>>() {

      @Override
      public void attempt() {
        ServicesLocator.getServiceMedia()
            .getAlbumsAndPictures(event.getInstanceId(), event.getRootAlbumId(), callNumber, this);
      }

      @Override
      public void onSuccess(final RestMethod method, final StreamingList<BaseDTO> result) {
        super.onSuccess(method, result);
        setStopLoading(false);
        currentAlbumsItems = result.getList();
        EventBus.getInstance().fireEvent(new DataLoadedEvent(event.getRootAlbumId(), result.getList()));
        if (result.getMoreElement() && !isStopLoading()) {
          loadNextPartAlbums(event.getInstanceId(), event.getRootAlbumId(), 1);
        } else {
          EventBus.getInstance().fireEvent(new LoadingDataFinishEvent());
        }
      }
    };
    action.attempt();
  }

  private void loadNextPartAlbums(String instanceId, String rootAlbumId, final int callNumber) {

    RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<StreamingList<BaseDTO>>() {

      @Override
      public void attempt() {
        ServicesLocator.getServiceMedia()
            .getAlbumsAndPictures(instanceId, rootAlbumId, callNumber, this);
      }

      @Override
      public void onSuccess(final RestMethod method, final StreamingList<BaseDTO> result) {
        super.onSuccess(method, result);
        currentAlbumsItems.addAll(result.getList());
        EventBus.getInstance().fireEvent(new MoreDataLoadedEvent(rootAlbumId, result.getList()));
        if (result.getMoreElement() && !isStopLoading()) {
          loadNextPartAlbums(instanceId, rootAlbumId, callNumber+1);
        } else {
          EventBus.getInstance().fireEvent(new LoadingDataFinishEvent());
        }
      }
    };
    action.attempt();

  }

  @Override
  public void loadMediaPreview(final MediaPreviewLoadEvent event) {
    if (event.getMedia() == null) {
      if (event.getContentType().equals(ContentsTypes.Photo.toString())) {

        RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<PhotoDTO>() {
          @Override
          public void attempt() {
            ServicesLocator.getServiceMedia()
                .getPreviewPicture(event.getInstanceId(), event.getMediaId(), this);
          }

          @Override
          public void onSuccess(final RestMethod method, final PhotoDTO preview) {
            super.onSuccess(method, preview);
            EventBus.getInstance()
                .fireEvent(new MediaPreviewLoadedEvent(preview, commentable, notifiable));
          }
        };
        action.attempt();

      } else if (event.getContentType().equals(ContentsTypes.Sound.toString())) {

        RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<SoundDTO>() {

          @Override
          public void attempt() {
            ServicesLocator.getServiceMedia()
                .getSound(event.getInstanceId(), event.getMediaId(), this);
          }

          @Override
          public void onSuccess(final RestMethod method, final SoundDTO sound) {
            super.onSuccess(method, sound);
            EventBus.getInstance()
                .fireEvent(new MediaPreviewLoadedEvent(sound, commentable, notifiable));
          }
        };
        action.attempt();

      } else if (event.getContentType().equals(ContentsTypes.Video.toString())) {

        RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<VideoDTO>() {

          @Override
          public void attempt() {
            ServicesLocator.getServiceMedia()
                .getVideo(event.getInstanceId(), event.getMediaId(), this);
          }

          @Override
          public void onSuccess(final RestMethod method, final VideoDTO video) {
            super.onSuccess(method, video);
            EventBus.getInstance()
                .fireEvent(new MediaPreviewLoadedEvent(video, commentable, notifiable));
          }
        };
        action.attempt();

      } else if (event.getContentType().equals(ContentsTypes.Streaming.toString())) {

        RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<VideoStreamingDTO>() {

          @Override
          public void attempt() {
            ServicesLocator.getServiceMedia()
                .getVideoStreaming(event.getInstanceId(), event.getMediaId(), this);
          }

          @Override
          public void onSuccess(final RestMethod method, final VideoStreamingDTO video) {
            super.onSuccess(method, video);
            EventBus.getInstance()
                .fireEvent(new MediaPreviewLoadedEvent(video, commentable, notifiable));
          }
        };
        action.attempt();
      }
    } else {
      EventBus.getInstance()
          .fireEvent(new MediaPreviewLoadedEvent(event.getMedia(), commentable, notifiable));
    }
  }

  @Override
  public void nextMediaView(final MediaViewGetNextEvent mediaViewNextEvent) {
    int index = findMediaIndex(mediaViewNextEvent.getCurrentMedia());
    for (int i = index + 1; i < currentAlbumsItems.size(); i++) {
      if (currentAlbumsItems.get(i) instanceof MediaDTO) {
        EventBus.getInstance()
            .fireEvent(new MediaViewNextEvent((MediaDTO) currentAlbumsItems.get(i)));
        return;
      }
    }
    EventBus.getInstance().fireEvent(new MediaViewNextEvent(mediaViewNextEvent.getCurrentMedia()));
  }

  @Override
  public void prevMediaView(final MediaViewGetPreviousEvent mediaViewPreviousEvent) {
    int index = findMediaIndex(mediaViewPreviousEvent.getCurrentMedia());
    for (int i = index - 1; i >= 0; i--) {
      if (currentAlbumsItems.get(i) instanceof MediaDTO) {
        EventBus.getInstance()
            .fireEvent(new MediaViewPrevEvent((MediaDTO) currentAlbumsItems.get(i)));
        return;
      }
    }
    EventBus.getInstance()
        .fireEvent(new MediaViewPrevEvent(mediaViewPreviousEvent.getCurrentMedia()));
  }

  private int findMediaIndex(MediaDTO media) {
    int i = 0;
    for (BaseDTO item : currentAlbumsItems) {
      if (item instanceof MediaDTO && item.getId().equals(media.getId())) {
        return i;
      }
      i++;
    }
    return -1;
  }
}
