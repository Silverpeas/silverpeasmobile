package com.silverpeas.mobile.client.apps.media.events.app;

import com.google.gwt.event.shared.EventHandler;

public interface MediaAppEventHandler extends EventHandler {
  void loadAlbums(MediasLoadMediaItemsEvent event);
  void loadMediaPreview(MediaPreviewLoadEvent event);
}