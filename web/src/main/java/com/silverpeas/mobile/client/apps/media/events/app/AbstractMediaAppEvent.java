package com.silverpeas.mobile.client.apps.media.events.app;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractMediaAppEvent extends GwtEvent<MediaAppEventHandler> {

  public static Type<MediaAppEventHandler> TYPE = new Type<MediaAppEventHandler>();

  public AbstractMediaAppEvent() {
  }

  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<MediaAppEventHandler> getAssociatedType() {
    return TYPE;
  }
}
