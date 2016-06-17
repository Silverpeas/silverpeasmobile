package com.silverpeas.mobile.client.components.base.events.apps;

import com.google.gwt.event.shared.EventHandler;

public interface AppEventHandler extends EventHandler {
    void receiveEvent(AppEvent event);
}
