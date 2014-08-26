package com.silverpeas.mobile.client.apps.media.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractMediaPagesEvent extends GwtEvent<MediaPagesEventHandler> {

	public static Type<MediaPagesEventHandler> TYPE = new Type<MediaPagesEventHandler>();

	public AbstractMediaPagesEvent() {
	}

	@Override
	public Type<MediaPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
