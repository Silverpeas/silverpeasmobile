package com.silverpeas.mobile.client.apps.media.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface MediaPagesEventHandler extends EventHandler {
	void onMediaPreviewLoaded(MediaPreviewLoadedEvent event);
  void onMediaViewLoaded(MediaViewLoadedEvent event);
}