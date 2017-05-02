package com.silverpeas.mobile.client.apps.media;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.media.events.app.AbstractMediaAppEvent;
import com.silverpeas.mobile.client.apps.media.events.app.MediaAppEventHandler;
import com.silverpeas.mobile.client.apps.media.events.app.MediaPreviewLoadEvent;
import com.silverpeas.mobile.client.apps.media.events.app.MediaViewGetNextEvent;
import com.silverpeas.mobile.client.apps.media.events.app.MediaViewGetPreviousEvent;
import com.silverpeas.mobile.client.apps.media.events.app.MediaViewLoadEvent;
import com.silverpeas.mobile.client.apps.media.events.app.MediaViewShowEvent;
import com.silverpeas.mobile.client.apps.media.events.app.MediasLoadMediaItemsEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaPreviewLoadedEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaViewLoadedEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaViewNextEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.MediaViewPrevEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.navigation.MediaItemsLoadedEvent;
import com.silverpeas.mobile.client.apps.media.pages.MediaNavigationPage;
import com.silverpeas.mobile.client.apps.media.pages.PhotoPage;
import com.silverpeas.mobile.client.apps.media.pages.SoundPage;
import com.silverpeas.mobile.client.apps.media.pages.VideoPage;
import com.silverpeas.mobile.client.apps.media.pages.VideoStreamingPage;
import com.silverpeas.mobile.client.apps.media.resources.MediaMessages;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external
    .NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.ContentDTO;
import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.media.MediaDTO;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;
import com.silverpeas.mobile.shared.dto.media.SoundDTO;
import com.silverpeas.mobile.shared.dto.media.VideoDTO;
import com.silverpeas.mobile.shared.dto.media.VideoStreamingDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.Apps;

import java.util.ArrayList;
import java.util.List;

public class MediaApp extends App implements NavigationEventHandler, MediaAppEventHandler {

  private MediaMessages msg;
  private ApplicationMessages globalMsg;
  //private NavigationApp navApp = new NavigationApp();

  // Model
  private boolean commentable, notifiable;
  private List<BaseDTO> currentAlbumsItems;

  public MediaApp() {
    super();
    msg = GWT.create(MediaMessages.class);
    globalMsg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(AbstractMediaAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  @Override
  public void start() {
    //navApp.setTypeApp(Apps.gallery.name());
    //navApp.setTitle(msg.title());
    //navApp.start();

    // apps main is navigation apps main page
    //setMainPage(navApp.getMainPage());

    // no "super.start(lauchingPage);" this apps is used in another apps
  }

  @Override
  public void startWithContent(final ContentDTO content) {
    ServicesLocator.getServiceNavigation()
        .getApp(content.getInstanceId(), content.getId(), content.getType(), new AsyncCallback<ApplicationInstanceDTO>() {
          @Override
          public void onFailure(final Throwable caught) {
            if (OfflineHelper.needToGoOffine(caught)) {
              Notification.alert(globalMsg.needToBeOnline());
            } else {
              EventBus.getInstance().fireEvent(new ErrorEvent(caught));
            }
          }

          @Override
          public void onSuccess(final ApplicationInstanceDTO app) {
            commentable = app.isCommentable();
            notifiable = app.isNotifiable();
            displayContent(content);
          }
        });
  }

  private void displayContent(ContentDTO contentSource) {
    MediaDTO content = null;

    if (contentSource.getType().equals(ContentsTypes.Photo.toString())) {
      content = new PhotoDTO();
    } else if (contentSource.getType().equals(ContentsTypes.Sound.toString())) {
      content = new SoundDTO();
    } else if (contentSource.getType().equals(ContentsTypes.Video.toString())) {
      content = new VideoDTO();
    } else if (contentSource.getType().equals(ContentsTypes.Streaming.toString())) {
      content = new VideoStreamingDTO();
    }
    content.setInstance(contentSource.getId());
    content.setInstance(contentSource.getInstanceId());
    displayContent(content);
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
    }
  }

  @Override
  public void loadAlbums(final MediasLoadMediaItemsEvent event) {
    final String key = "album_" + event.getInstanceId() + "_" + event.getRootAlbumId();
    Command offlineAction = new Command() {

      @Override
      public void execute() {
        List<BaseDTO> result = LocalStorageHelper.load(key, List.class);
        if (result == null) {
          result = new ArrayList<BaseDTO>();
        }
        EventBus.getInstance().fireEvent(new MediaItemsLoadedEvent(result));
      }
    };


    AsyncCallbackOnlineOrOffline action =
        new AsyncCallbackOnlineOrOffline<List<BaseDTO>>(offlineAction) {
          @Override
          public void attempt() {
            ServicesLocator.getServiceMedia()
                .getAlbumsAndPictures(event.getInstanceId(), event.getRootAlbumId(), this);
          }

          @Override
          public void onSuccess(List<BaseDTO> result) {
            super.onSuccess(result);
            LocalStorageHelper.store(key, List.class, result);
            currentAlbumsItems = result;
            EventBus.getInstance().fireEvent(new MediaItemsLoadedEvent(result));
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
    final String key = "picture_" + event.getMediaId();
    Command offlineAction = new Command() {
      @Override
      public void execute() {
        PhotoDTO view = LocalStorageHelper.load(key, PhotoDTO.class);
        EventBus.getInstance().fireEvent(new MediaViewLoadedEvent(view));
      }
    };

    AsyncCallbackOnlineOrOffline action =
        new AsyncCallbackOnlineOrOffline<PhotoDTO>(offlineAction) {
          @Override
          public void attempt() {
            ServicesLocator.getServiceMedia()
                .getOriginalPicture(event.getInstanceId(), event.getMediaId(), this);
          }

          @Override
          public void onSuccess(final PhotoDTO view) {
            super.onSuccess(view);
            LocalStorageHelper.store(key, PhotoDTO.class, view);
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
