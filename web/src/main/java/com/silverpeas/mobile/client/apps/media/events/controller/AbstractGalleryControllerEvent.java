package com.silverpeas.mobile.client.apps.media.events.controller;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractGalleryControllerEvent extends GwtEvent<GalleryControllerEventHandler> {

	public static Type<GalleryControllerEventHandler> TYPE = new Type<GalleryControllerEventHandler>();
	
	public AbstractGalleryControllerEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GalleryControllerEventHandler> getAssociatedType() {
		return TYPE;
	}
}
