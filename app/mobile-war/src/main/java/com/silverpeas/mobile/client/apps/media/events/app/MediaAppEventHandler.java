package com.silverpeas.mobile.client.apps.media.events.app;

import com.google.gwt.event.shared.EventHandler;

public interface MediaAppEventHandler extends EventHandler {
  void loadAlbums(MediasLoadMediaItemsEvent event);
  void loadMediaPreview(MediaPreviewLoadEvent event);
  void loadMediaView(MediaViewLoadEvent event);
  void nextMediaView(MediaViewGetNextEvent mediaViewNextEvent);
  void prevMediaView(MediaViewGetPreviousEvent mediaViePreviousEvent);
  void loadMediaShow(MediaViewShowEvent mediaViewShowEvent);
  void stopLoadingAlbums(StopMediaLoadingEvent stopMediaLoadingEvent);
}