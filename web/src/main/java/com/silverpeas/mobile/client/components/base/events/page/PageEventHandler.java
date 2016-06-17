package com.silverpeas.mobile.client.components.base.events.page;

import com.google.gwt.event.shared.EventHandler;

public interface PageEventHandler extends EventHandler {
    void receiveEvent(PageEvent event);
}
