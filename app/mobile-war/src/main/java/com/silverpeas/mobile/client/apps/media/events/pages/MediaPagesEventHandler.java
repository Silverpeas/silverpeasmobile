package com.silverpeas.mobile.client.apps.media.events.pages;

import com.google.gwt.event.shared.EventHandler;
import com.silverpeas.mobile.client.apps.media.events.pages.navigation.NoMoreMediaToLoadEvent;

public interface MediaPagesEventHandler extends EventHandler {
	void onMediaPreviewLoaded(MediaPreviewLoadedEvent event);
  void onMediaViewLoaded(MediaViewLoadedEvent event);

  void onMediaViewNext(MediaViewNextEvent mediaViewNextEvent);

  void onMediaViewPrev(MediaViewPrevEvent mediaViewPrevEvent);
}