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

package org.silverpeas.mobile.client.apps.media;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.silverpeas.mobile.client.apps.media.events.app.AbstractMediaAppEvent;
import org.silverpeas.mobile.client.apps.media.events.app.MediaAppEventHandler;
import org.silverpeas.mobile.client.apps.media.events.app.MediaPreviewLoadEvent;
import org.silverpeas.mobile.client.apps.media.events.app.MediaViewGetNextEvent;
import org.silverpeas.mobile.client.apps.media.events.app.MediaViewGetPreviousEvent;
import org.silverpeas.mobile.client.apps.media.events.app.MediaViewLoadEvent;
import org.silverpeas.mobile.client.apps.media.events.app.MediaViewShowEvent;
import org.silverpeas.mobile.client.apps.media.events.app.MediasLoadMediaItemsEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaPreviewLoadedEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaViewLoadedEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaViewNextEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.MediaViewPrevEvent;
import org.silverpeas.mobile.client.apps.media.pages.MediaNavigationPage;
import org.silverpeas.mobile.client.apps.media.pages.PhotoPage;
import org.silverpeas.mobile.client.apps.media.pages.SoundPage;
import org.silverpeas.mobile.client.apps.media.pages.VideoPage;
import org.silverpeas.mobile.client.apps.media.pages.VideoStreamingPage;
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
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.network.OfflineHelper;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.client.components.base.events.page.DataLoadedEvent;
import org.silverpeas.mobile.client.components.base.events.page.LoadingDataFinishEvent;
import org.silverpeas.mobile.client.components.base.events.page.MoreDataLoadedEvent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.media.MediaDTO;
import org.silverpeas.mobile.shared.dto.media.PhotoDTO;
import org.silverpeas.mobile.shared.dto.media.SoundDTO;
import org.silverpeas.mobile.shared.dto.media.VideoDTO;
import org.silverpeas.mobile.shared.dto.media.VideoStreamingDTO;
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
    ServicesLocator.getServiceNavigation()
        .getApp(content.getInstanceId(), content.getId(), content.getType(), new AsyncCallback<ApplicationInstanceDTO>() {
          @Override
          public void onFailure(final Throwable caught) {
            Notification.activityStop();
            if (OfflineHelper.needToGoOffine(caught)) {
              Notification.alert(globalMsg.needToBeOnline());
            } else {
              EventBus.getInstance().fireEvent(new ErrorEvent(caught));
            }
          }

          @Override
          public void onSuccess(final ApplicationInstanceDTO app) {
            Notification.activityStop();
            commentable = app.isCommentable();
            notifiable = app.isNotifiable();
            displayContent(content);
          }
        });
  }

  private void displayContent(ContentDTO contentSource) {
    ServicesLocator.getServiceMedia().getMedia(contentSource.getId(), new AsyncCallbackOnlineOnly
        <MediaDTO>() {
      @Override
      public void attempt() {}

      @Override
      public void onSuccess(final MediaDTO media) {
        super.onSuccess(media);
        displayContent(media);
      }
    });
  }

  private void displayContent(final MediaDTO item) {
    if (item instanceof PhotoDTO) {
      PhotoPage page = new PhotoPage();
      page.setPageTitle(msg.title());
      page.show();
      EventBus.getInstance().fireEvent(
          new MediaPreviewLoadEvent(item.getInstance(), ContentsTypes.Photo.toString(),
              item.getId(), null));
    } else if (item instanceof SoundDTO) {
      SoundPage page = new SoundPage();
      page.setPageTitle(msg.title());
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
      this.commentable = event.getInstance().isCommentable();
      this.notifiable = event.getInstance().isNotifiable();
      MediaNavigationPage page = new MediaNavigationPage();
      page.setApp(this);
      page.setPageTitle(event.getInstance().getLabel());
      page.init(event.getInstance().getId(), null, event.getInstance().getRights());
      page.show();
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals(ContentsTypes.Media.name()) ||
        event.getContent().getType().equals(ContentsTypes.Photo.name()) ||
        event.getContent().getType().equals(ContentsTypes.Sound.name()) ||
        event.getContent().getType().equals(ContentsTypes.Video.name()) ||
        event.getContent().getType().equals(ContentsTypes.Streaming.name())) {
      startWithContent(event.getContent());
    } else if (event.getContent().getType().equals(ContentsTypes.Album.name())) {
      ServicesLocator.getServiceNavigation().getApp(event.getContent().getInstanceId(), null, null,
          new AsyncCallback<ApplicationInstanceDTO>() {
            @Override
            public void onFailure(final Throwable caught) {
              Notification.activityStop();
              EventBus.getInstance().fireEvent(new ErrorEvent(caught));
            }

            @Override
            public void onSuccess(final ApplicationInstanceDTO applicationInstanceDTO) {
              Notification.activityStop();
              MediaNavigationPage page = new MediaNavigationPage();
              page.setApp(MediaApp.this);
              page.init(event.getContent().getInstanceId(), event.getContent().getId(), applicationInstanceDTO.getRights());
              page.show();
            }
          });
    }
  }

  @Override
  public void loadAlbums(final MediasLoadMediaItemsEvent event) {
    final String key = "album_" + event.getInstanceId() + "_" + event.getRootAlbumId();
    Command offlineAction = new Command() {

      @Override
      public void execute() {
        StreamingList<BaseDTO> result = LocalStorageHelper.load(key, StreamingList.class);
        if (result == null) {
          result = new StreamingList<BaseDTO>();
        }
        EventBus.getInstance().fireEvent(new DataLoadedEvent(event.getRootAlbumId(), result));
      }
    };
    boolean moreElements = true;
    int callNumber = 0;

    AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<StreamingList<BaseDTO>>(offlineAction) {
      @Override
      public void attempt() {
        ServicesLocator.getServiceMedia()
            .getAlbumsAndPictures(event.getInstanceId(), event.getRootAlbumId(), callNumber, this);
      }

      @Override
      public void onSuccess(StreamingList<BaseDTO> result) {
        super.onSuccess(result);
        setStopLoading(false);
        LocalStorageHelper.store(key, List.class, result);
        currentAlbumsItems = result;
        EventBus.getInstance().fireEvent(new DataLoadedEvent(event.getRootAlbumId(), result));
        if (result.hasMoreElement() && !isStopLoading()) {
          loadNextPartAlbums(event.getInstanceId(), event.getRootAlbumId(), 1, key);
        } else {
          EventBus.getInstance().fireEvent(new LoadingDataFinishEvent());
        }
      }
    };
    action.attempt();
  }

  private void loadNextPartAlbums(String instanceId, String rootAlbumId, final int callNumber, final String key) {
    AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<StreamingList<BaseDTO>>(null) {
      @Override
      public void attempt() {
        ServicesLocator.getServiceMedia(false)
            .getAlbumsAndPictures(instanceId, rootAlbumId, callNumber, this);
      }

      @Override
      public void onSuccess(StreamingList<BaseDTO> result) {
        super.onSuccess(result);
        currentAlbumsItems.addAll(result);
        LocalStorageHelper.store(key, List.class, currentAlbumsItems);
        EventBus.getInstance().fireEvent(new MoreDataLoadedEvent(rootAlbumId, result));
        if (result.hasMoreElement() && !isStopLoading()) {
          loadNextPartAlbums(instanceId, rootAlbumId, callNumber+1, key);
        } else {
          EventBus.getInstance().fireEvent(new LoadingDataFinishEvent());
        }
      }

    };
    action.attempt();
  }

  @Override
  public void loadMediaPreview(final MediaPreviewLoadEvent event) {
    final String key = "media_" + event.getContentType() + "_" + event.getMediaId();
    if (event.getMedia() == null) {
      if (event.getContentType().equals(ContentsTypes.Photo.toString())) {
        Command offlineAction = new Command() {
          @Override
          public void execute() {
            PhotoDTO preview = LocalStorageHelper.load(key, PhotoDTO.class);
            EventBus.getInstance()
                .fireEvent(new MediaPreviewLoadedEvent(preview, commentable, notifiable));
          }
        };
        AsyncCallbackOnlineOrOffline action =
            new AsyncCallbackOnlineOrOffline<PhotoDTO>(offlineAction) {
              @Override
              public void attempt() {
                ServicesLocator.getServiceMedia()
                    .getPreviewPicture(event.getInstanceId(), event.getMediaId(), this);
              }

              @Override
              public void onSuccess(final PhotoDTO preview) {
                super.onSuccess(preview);
                LocalStorageHelper.store(key, PhotoDTO.class, preview);
                EventBus.getInstance()
                    .fireEvent(new MediaPreviewLoadedEvent(preview, commentable, notifiable));
              }
            };
        action.attempt();
      } else if (event.getContentType().equals(ContentsTypes.Sound.toString())) {
        Command offlineAction = new Command() {
          @Override
          public void execute() {
            SoundDTO sound = LocalStorageHelper.load(key, SoundDTO.class);
            EventBus.getInstance()
                .fireEvent(new MediaPreviewLoadedEvent(sound, commentable, notifiable));
          }
        };
        AsyncCallbackOnlineOrOffline action =
            new AsyncCallbackOnlineOrOffline<SoundDTO>(offlineAction) {
              @Override
              public void attempt() {
                ServicesLocator.getServiceMedia()
                    .getSound(event.getInstanceId(), event.getMediaId(), this);
              }

              @Override
              public void onSuccess(final SoundDTO sound) {
                super.onSuccess(sound);
                LocalStorageHelper.store(key, SoundDTO.class, sound);
                EventBus.getInstance()
                    .fireEvent(new MediaPreviewLoadedEvent(sound, commentable, notifiable));
              }
            };
        action.attempt();
      } else if (event.getContentType().equals(ContentsTypes.Video.toString())) {
        Command offlineAction = new Command() {
          @Override
          public void execute() {
            VideoDTO video = LocalStorageHelper.load(key, VideoDTO.class);
            EventBus.getInstance()
                .fireEvent(new MediaPreviewLoadedEvent(video, commentable, notifiable));
          }
        };
        AsyncCallbackOnlineOrOffline action =
            new AsyncCallbackOnlineOrOffline<VideoDTO>(offlineAction) {
              @Override
              public void attempt() {
                ServicesLocator.getServiceMedia()
                    .getVideo(event.getInstanceId(), event.getMediaId(), this);
              }

              @Override
              public void onSuccess(final VideoDTO video) {
                super.onSuccess(video);
                LocalStorageHelper.store(key, VideoDTO.class, video);
                EventBus.getInstance()
                    .fireEvent(new MediaPreviewLoadedEvent(video, commentable, notifiable));
              }
            };
        action.attempt();
      } else if (event.getContentType().equals(ContentsTypes.Streaming.toString())) {
        Command offlineAction = new Command() {
          @Override
          public void execute() {
            VideoStreamingDTO video = LocalStorageHelper.load(key, VideoStreamingDTO.class);
            EventBus.getInstance()
                .fireEvent(new MediaPreviewLoadedEvent(video, commentable, notifiable));
          }
        };
        AsyncCallbackOnlineOrOffline action =
            new AsyncCallbackOnlineOrOffline<VideoStreamingDTO>(offlineAction) {
              @Override
              public void attempt() {
                ServicesLocator.getServiceMedia()
                    .getVideoStreaming(event.getInstanceId(), event.getMediaId(), this);
              }

              @Override
              public void onSuccess(final VideoStreamingDTO video) {
                super.onSuccess(video);
                LocalStorageHelper.store(key, VideoStreamingDTO.class, video);
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
  public void loadMediaView(final MediaViewLoadEvent event) {
    AsyncCallbackOnlineOnly action =
        new AsyncCallbackOnlineOnly<PhotoDTO>() {
          @Override
          public void attempt() {
            ServicesLocator.getServiceMedia()
                .getOriginalPicture(event.getInstanceId(), event.getMediaId(), this);
          }

          @Override
          public void onSuccess(final PhotoDTO view) {
            super.onSuccess(view);
            EventBus.getInstance().fireEvent(new MediaViewLoadedEvent(view));
          }
        };
    action.attempt();
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
