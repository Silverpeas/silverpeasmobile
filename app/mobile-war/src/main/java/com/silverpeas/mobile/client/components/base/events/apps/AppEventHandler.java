package com.silverpeas.mobile.client.components.base.events.apps;

import com.google.gwt.event.shared.EventHandler;
import com.silverpeas.mobile.client.components.base.events.page.DataLoadedEvent;

public interface AppEventHandler extends EventHandler {
  void receiveEvent(AppEvent event);

  void stopLoadingDataEvent(StopLoadingDataEvent stopLoadingDataEvent);

}
