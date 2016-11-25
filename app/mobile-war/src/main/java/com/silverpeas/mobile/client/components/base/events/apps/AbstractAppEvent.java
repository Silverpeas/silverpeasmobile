package com.silverpeas.mobile.client.components.base.events.apps;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractAppEvent extends GwtEvent<AppEventHandler> {

    public static Type<AppEventHandler> TYPE = new Type<AppEventHandler>();

    public AbstractAppEvent() {
    }

    @Override
    public Type<AppEventHandler> getAssociatedType() {
        return TYPE;
    }
}
