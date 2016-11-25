package com.silverpeas.mobile.client.components.base.events.page;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractPageEvent extends GwtEvent<PageEventHandler> {

    public static Type<PageEventHandler> TYPE = new Type<PageEventHandler>();

    public AbstractPageEvent() {
    }

    @Override
    public Type<PageEventHandler> getAssociatedType() {
        return TYPE;
    }
}
