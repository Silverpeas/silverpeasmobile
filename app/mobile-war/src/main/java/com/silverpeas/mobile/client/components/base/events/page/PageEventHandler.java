package com.silverpeas.mobile.client.components.base.events.page;

import com.google.gwt.event.shared.EventHandler;

public interface PageEventHandler extends EventHandler {
    void receiveEvent(PageEvent event);

  void finishLoadingData(LoadingDataFinishEvent loadingDataFinishEvent);

  void loadedDataEvent(DataLoadedEvent dataLoadedEvent);

  void loadedMoreDataEvent(MoreDataLoadedEvent moreDataLoadedEvent);
}
